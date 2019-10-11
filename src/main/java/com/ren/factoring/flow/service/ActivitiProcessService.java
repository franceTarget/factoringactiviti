package com.ren.factoring.flow.service;

import com.ren.factoring.flow.models.model.ActGeBytearray;
import com.ren.factoring.flow.models.model.ActRuTask;
import com.ren.factoring.flow.models.request.DeployReq;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;

/**
 * @author: target
 * @date: 2019/9/18 9:14
 * @description:
 */
public interface ActivitiProcessService {

    public Deployment deploy(DeployReq req);

    public ActGeBytearray findByDeploymentId(String id);

    ActRuTask getCurrentTask(String procInstId);

    TaskDefinition getNextTask(String taskId, String elString);
}
