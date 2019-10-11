package com.ren.factoring.flow.service.impl;

import com.ren.factoring.flow.dao.ActGeBytearrayDao;
import com.ren.factoring.flow.models.model.ActGeBytearray;
import com.ren.factoring.flow.models.response.NodeResp;
import com.ren.factoring.flow.service.ActivitiByteService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ActivitiByteServiceImpl implements ActivitiByteService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActGeBytearrayDao actGeBytearrayDao;

    @Override
    public List<ActivityImpl> getAllNodeInfo(String definitionId) {

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        return activitiList;
    }

    @Override
    public List<NodeResp> getAllTaskNode(String definitionId) {
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
        List<NodeResp> list = new ArrayList<>();
        for (ActivityImpl activityImpl : activitiList) {
            String preNode = activityImpl.getId();
            //获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination();
                NodeResp nodeResp = new NodeResp();
                nodeResp.setPreNode(preNode);
                nodeResp.setNextNode(ac.getId());
                nodeResp.setType(ac.getProperty("type").toString());
                nodeResp.setEl(null != tr.getProperty("conditionText") ? tr.getProperty("conditionText").toString()
                        : "");
                list.add(nodeResp);
            }
        }
        return list;
    }

    @Override
    public ActGeBytearray getByteArray(String deploymentId) {
        return actGeBytearrayDao.selectByDeployId(deploymentId);
    }
}
