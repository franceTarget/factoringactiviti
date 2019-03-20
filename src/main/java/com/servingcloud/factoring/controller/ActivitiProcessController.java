package com.servingcloud.factoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.servingcloud.factoring.models.request.DeployReq;
import com.servingcloud.factoring.models.response.Response;
import com.servingcloud.factoring.utils.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(tags = "流程接口")
@RestController("/process")
public class ActivitiProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation(value = "流程发布", notes = "")
    @RequestMapping(value = "/deploy", method = POST)
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
        ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
        Deployment deployment = repositoryService.createDeployment().name(req.getName())
                .addInputStream(processName, in).deploy();

        return Response.ok("", deployment.getId());
    }
}
