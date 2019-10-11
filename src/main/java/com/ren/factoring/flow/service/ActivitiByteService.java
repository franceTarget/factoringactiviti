package com.ren.factoring.flow.service;

import com.ren.factoring.flow.models.model.ActGeBytearray;
import com.ren.factoring.flow.models.response.NodeResp;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

public interface ActivitiByteService {
    List<ActivityImpl> getAllNodeInfo(String definitionId);

    List<NodeResp> getAllTaskNode(String definitionId);

    ActGeBytearray getByteArray(String deploymentId);
}
