package com.ren.factoring.vanke.listener;

import com.ren.factoring.vanke.config.SpringContextHolder;
import com.ren.factoring.vanke.handler.AutoCompleteCmd;
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

@Slf4j
public class AutoCompleteFirstTaskEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        log.info("--------监听任务------");
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
                case TASK_CREATED:
                    this.onCreate(taskEntity);
                    break;
            }
        } catch (Exception ex) {
        }
    }

    private void onCreate(DelegateTask delegateTask) throws Exception {
        //如果是流程的第一步，则自动提交
        PvmActivity targetActivity = findFirstActivity(delegateTask.getProcessDefinitionId());

        if (!targetActivity.getId().equals(delegateTask.getExecution().getCurrentActivityId())) {
            return;
        }

        new AutoCompleteCmd(delegateTask.getId(),delegateTask.getVariables(),"发起流程").execute(Context.getCommandContext());
    }

    private PvmActivity findFirstActivity(String processDefinitionId) {
        ProcessDefinitionEntity processDefinitionEntity  = (ProcessDefinitionEntity) getRepositoryService().getProcessDefinition(processDefinitionId);

        ActivityImpl startActivity = processDefinitionEntity.getInitial();

        PvmTransition pvmTransition = startActivity.getOutgoingTransitions().get(0);
        PvmActivity targetActivity = pvmTransition.getDestination();

        if (!"userTask".equals(targetActivity.getProperty("type"))) {
            return null;
        }
        return targetActivity;
    }

    public RepositoryService getRepositoryService(){
        return SpringContextHolder.getBean(RepositoryService.class);
    }


    @Override
    public boolean isFailOnException() {
        return false;
    }
}
