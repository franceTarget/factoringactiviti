package com.ren.factoring.flow.controller;

import com.google.common.collect.Maps;
import com.ren.factoring.flow.models.request.CompleteBaseReq;
import com.ren.factoring.flow.models.request.DeployReq;
import com.ren.factoring.flow.models.request.StartReq;
import com.ren.factoring.flow.models.response.Response;
import com.ren.factoring.flow.service.ActivitiProcessService;
import com.ren.factoring.flow.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private IdentityService identityService;

    @ApiOperation(value = "流程发布", notes = "")
    @RequestMapping(value = "/self/process/deploy", method = POST)
    public Response<String> deploy(@RequestBody DeployReq req) {
        Deployment deployment = activitiProcessService.deploy(req);
        return Response.ok("发布成功", deployment.getId());
    }

    @ApiOperation(value = "通过发布key启动流程", notes = "")
    @RequestMapping(value = "/self/process/start/key", method = POST)
    public Response<String> startByKey(@RequestBody StartReq req) {
        //设置流程发起人
        identityService.setAuthenticatedUserId(req.getStartActId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(req.getProcDefkey(), req.getBusinessKey(), req.getTenantId());
        return Response.ok("启动成功", processInstance.getProcessInstanceId());
    }

    @ApiOperation(value = "通过发布id的启动流程", notes = "")
    @RequestMapping(value = "/self/process/start/id", method = POST)
    public Response<String> startById(@RequestBody StartReq req) {
        Map<String, Object> map = Maps.newHashMap();
        //设置流程发起人
        identityService.setAuthenticatedUserId(req.getStartActId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(req.getProcDefId(), req.getBusinessKey(), map);
        return Response.ok("启动成功", processInstance.getProcessInstanceId());
    }

    @ApiOperation(value = "任务提交", notes = "")
    @RequestMapping(value = "/self/process/complete", method = POST)
    public Response<Boolean> complete(@RequestBody CompleteBaseReq req) {
        //添加批注
        if (StringUtil.isNotEmpty(req.getComment())) {
            taskService.addComment(req.getTaskId(), req.getProcessId(), req.getComment());
        }

        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtil.isNotEmpty(req.getBranchCondition())) {
            resultMap.put("branch_condition", req.getBranchCondition());
        }
        if (StringUtil.isNotEmpty(req.getUserId())) {
            taskService.setAssignee(req.getTaskId(), req.getUserId());
        }
        taskService.complete(req.getTaskId(), resultMap);
        return Response.ok("提交成功", true);
    }
}
