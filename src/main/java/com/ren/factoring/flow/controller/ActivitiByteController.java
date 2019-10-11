package com.ren.factoring.flow.controller;

import com.ren.factoring.flow.models.model.ActGeBytearray;
import com.ren.factoring.flow.models.response.NodeResp;
import com.ren.factoring.flow.models.response.Response;
import com.ren.factoring.flow.service.ActivitiByteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api(tags = "流程节点信息接口")
@RestController
public class ActivitiByteController {

    @Autowired
    private ActivitiByteService activitiByteService;

    @ApiOperation(value = "所有流程节点获取", notes = "")
    @RequestMapping(value = "/all/node/info/query", method = GET)
    public Response<List<ActivityImpl>> getAllNodeInfo(@ApiParam("流程发布Id") @RequestParam("definitionId") String definitionId) {
        return Response.ok("", activitiByteService.getAllNodeInfo(definitionId));
    }

    @ApiOperation(value = "所有流程任务节点获取", notes = "")
    @RequestMapping(value = "/all/task/node/query", method = GET)
    public Response<List<NodeResp>> getAllTaskNode(@ApiParam("流程发布Id") @RequestParam("definitionId") String definitionId) {
        return Response.ok("", activitiByteService.getAllTaskNode(definitionId));
    }

    @ApiOperation(value = "流程xml数据", notes = "")
    @RequestMapping(value = "/byte/array/query", method = GET)
    public Response<ActGeBytearray> getByteArray(@ApiParam("流程发布Id") @RequestParam("deploymentId") String deploymentId) {
        return Response.ok("", activitiByteService.getByteArray(deploymentId));
    }
}
