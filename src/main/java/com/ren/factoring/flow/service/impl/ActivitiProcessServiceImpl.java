package com.ren.factoring.flow.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ren.factoring.flow.models.request.DeployReq;
import com.ren.factoring.flow.service.ActivitiProcessService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author: target
 * @date: 2019/9/18 9:15
 * @description:
 */
@Service
public class ActivitiProcessServiceImpl implements ActivitiProcessService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * @author: target
     * @date: 2019/5/13 10:13
     * @description:部署流程
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Deployment deploy(DeployReq req) {
        //请假流程的模型
        Model model = repositoryService.createModelQuery().modelKey(req.getModleKey()).singleResult();
        //数据库保存的是模型的元数据，不是XMl格式--需要将元数据转换为XML格式，再进行部署

        byte[] modelEditorSource = repositoryService.getModelEditorSource(model.getId());

        ObjectNode modelNode = null;
        try {
            modelNode = (ObjectNode) new ObjectMapper().readTree(modelEditorSource);
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
        return deployment;
    }

}
