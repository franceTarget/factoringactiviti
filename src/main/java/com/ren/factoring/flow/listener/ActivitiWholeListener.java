package com.ren.factoring.flow.listener;

import com.ren.factoring.flow.config.AutoCompleteCmd;
import com.ren.factoring.flow.config.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * @author: target
 * @date: 2019/5/13 14:17
 * @description:全局事件监听
 */
@Slf4j
public class ActivitiWholeListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        log.info("--------全局监听任务------");
        if (!(activitiEvent instanceof ActivitiEntityEventImpl)) {
            return;
        }

        ActivitiEntityEventImpl activitiEntityEventImpl = (ActivitiEntityEventImpl) activitiEvent;
        Object entity = activitiEntityEventImpl.getEntity();

        if (!(entity instanceof TaskEntity)) {
            return;
        }

        TaskEntity taskEntity = (TaskEntity) entity;

        try {
            switch (activitiEvent.getType()) {
                //任务创建
                case TASK_CREATED:
//                    this.onCreate(taskEntity);
                    this.onMessage(taskEntity);
                    break;
                //任务提交
//                case TASK_COMPLETED:
//                    break;
            }
        } catch (Exception ex) {
            log.info("error ------[{}]------", ex);
        }
    }

    private void onMessage(TaskEntity taskEntity) {
        String id = taskEntity.getId();
        log.info("-------------->" + id);
    }

    private void onCreate(DelegateTask delegateTask) throws Exception {
        //如果是流程的第一步，则自动提交
        PvmActivity targetActivity = findFirstActivity(delegateTask.getProcessDefinitionId());

        if (!targetActivity.getId().equals(delegateTask.getExecution().getCurrentActivityId())) {
            return;
        }

        new AutoCompleteCmd(delegateTask.getId(), delegateTask.getVariables(), "发起流程").execute(Context.getCommandContext());
    }

    private PvmActivity findFirstActivity(String processDefinitionId) {
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) getRepositoryService().getProcessDefinition(processDefinitionId);

        ActivityImpl startActivity = processDefinitionEntity.getInitial();

        PvmTransition pvmTransition = startActivity.getOutgoingTransitions().get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();

        if (!"userTask".equals(targetActivity.getProperty("type"))) {
            return null;
        }
        return targetActivity;
    }

    public RepositoryService getRepositoryService() {
        return SpringContextHolder.getBean(RepositoryService.class);
    }


    @Override
    public boolean isFailOnException() {
        return false;
    }
}
