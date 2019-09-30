package com.ren.factoring.flow.service;

import com.ren.factoring.flow.models.request.DeployReq;
import org.activiti.engine.repository.Deployment;

/**
 * @author: target
 * @date: 2019/9/18 9:14
 * @description:
 */
public interface ActivitiProcessService {

    public Deployment deploy(DeployReq req);

}
