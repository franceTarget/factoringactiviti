package com.ren.factoring.vanke.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.ren.factoring.vanke.models.request.CompleteReq;
import com.ren.factoring.vanke.models.request.DeployReq;
import com.ren.factoring.vanke.models.request.StartReq;
import com.ren.factoring.vanke.models.response.JsonResponse;
import com.ren.factoring.vanke.models.response.Response;
import com.ren.factoring.vanke.utils.DefaultProcessDiagramGeneratorExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(tags = "流程接口")
@RestController
public class ActivitiProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "流程发布", notes = "")
    @RequestMapping(value = "/self-process/deploy", method = POST)
    public Response<String> deploy(@RequestBody DeployReq req) {

        //请假流程的模型
        Model model = repositoryService.createModelQuery().modelKey(req.getModleKey()).singleResult();
        //数据库保存的是模型的元数据，不是XMl格式--需要将元数据转换为XML格式，再进行部署
        ObjectNode modelNode = null;
        try {
            modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);

        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel, "UTF-8");

        String processName = model.getName() + ".bpmn20.xml";

        //部署流程
        Deployment deployment = null;
        try {
            deployment = repositoryService.createDeployment().name(req.getName()).tenantId(req.getTenantId()).addString(
                    processName, new String(bpmnBytes, "UTF-8")).deploy();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.ok("发布成功", deployment.getId());
    }

    @ApiOperation(value = "启动流程", notes = "")
    @RequestMapping(value = "/self-process/start", method = POST)
    public Response<String> start(@RequestBody StartReq req) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(req.getDeploymentId(), req.getBusinessId(), req.getTenantId());
        return Response.ok("启动成功", processInstance.getProcessInstanceId());
    }

    @ApiOperation(value = "任务提交", notes = "")
    @RequestMapping(value = "/self-process/complete", method = POST)
    public Response<Boolean> complete(@RequestBody CompleteReq req) {
        String task = req.getTask();
        String s = new RestTemplate().getForObject("http://192.168.11.43:8081/rules/taskTest?task=" + task, String.class);
        JsonResponse jsonResponse = JSONObject.parseObject(s, JsonResponse.class);
        Boolean data = (Boolean) jsonResponse.getData();
        String agree = "0";
        if (data) {
            agree = "1";
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("agree", agree);
        resultMap.put("context", req.getContext());
        taskService.setVariablesLocal(req.getTaskId(), resultMap);
        taskService.setAssignee(req.getTaskId(), req.getUserId());
        taskService.complete(req.getTaskId(), resultMap);

        return Response.ok("提交成功", true);
    }

    @GetMapping("/graphHistory/processInstance")
    public void processTracking(String processInstanceId, HttpServletResponse response) throws Exception {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String procDefId = processInstance.getProcessDefinitionId();

        // 当前活动节点、活动线
        List<String> activeActivityIds = new ArrayList<>(), highLightedFlows;
        //所有的历史活动节点
        List<String> highLightedFinishes = new ArrayList<>();

        // 如果流程已经结束，则得到结束节点
        if (!isFinished(processInstanceId)) {
            // 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        }

        // 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            highLightedFinishes.add(historicActivityInstance.getActivityId());
        }
        // 计算活动线
        highLightedFlows = getHighLightedFlows(
                (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(procDefId),
                historicActivityInstances);

        if (null != activeActivityIds) {
            InputStream imageStream = null;
            try {
                response.setContentType("image/png");

                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                // 获得流程引擎配置
                ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
                // 根据流程定义ID获得BpmnModel
                BpmnModel bpmnModel = repositoryService
                        .getBpmnModel(procDefId);
                // 输出资源内容到相应对象
                imageStream = new DefaultProcessDiagramGeneratorExt().generateDiagram(
                        bpmnModel,
                        "png",
                        highLightedFinishes,//所有活动过的节点，包括当前在激活状态下的节点
                        activeActivityIds,//当前为激活状态下的节点
                        highLightedFlows,//活动过的线
                        "宋体",
                        "宋体",
                        "宋体",
                        processEngineConfiguration.getClassLoader(),
                        1.0);

                int len;
                byte[] b = new byte[1024];

                while ((len = imageStream.read(b, 0, 1024)) != -1) {
                    response.getOutputStream().write(b, 0, len);
                }
            } finally {
                if (imageStream != null) {
                    imageStream.close();
                }
            }
        }
    }

    public boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
    }

    /**
     * @param processDefinitionEntity   流程定义实例
     * @param historicActivityInstances 流程活动节点实例
     * @author H.J
     * @date 2018/4/9 10:29
     * @title getHighLightedFlows
     * @description: 获取流程应该高亮的线
     * @return: java.util.List<java.lang.String>
     */
    public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
        List<String> highActivitiImpl = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            highActivitiImpl.add(historicActivityInstance.getActivityId());
        }

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstance.getActivityId());
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            // 对所有的线进行遍历
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (highActivitiImpl.contains(pvmActivityImpl.getId())) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }

        return highFlows;
    }
}
