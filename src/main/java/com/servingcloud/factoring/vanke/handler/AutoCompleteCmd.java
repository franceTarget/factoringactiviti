package com.servingcloud.factoring.vanke.handler;

import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLinkType;

import java.util.Map;

public class AutoCompleteCmd implements Command<Object> {

    private String taskId;
    private String comment;
    private Map<String,Object> variables;

    public AutoCompleteCmd(String taskId,Map<String,Object> variables,String comment){
        this.taskId = taskId;
        this.variables = variables;
        this.comment = comment;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        TaskEntity taskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);

        if(variables != null){
            taskEntity.setExecutionVariables(variables);
        }

        taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);

        if(Authentication.getAuthenticatedUserId() != null && taskEntity.getProcessInstanceId() != null){
            taskEntity.getProcessInstance().involveUser(Authentication.getAuthenticatedUserId(), IdentityLinkType.PARTICIPANT);
        }

        Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity,comment,false);

        if(taskEntity.getExecutionId() != null){
            ExecutionEntity executionEntity = taskEntity.getExecution();
            executionEntity.removeTask(taskEntity);
            executionEntity.signal(null,null);
        }
        return null;
    }
}
