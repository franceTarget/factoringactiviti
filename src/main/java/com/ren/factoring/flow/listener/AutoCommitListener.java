package com.ren.factoring.flow.listener;

import com.ren.factoring.flow.config.AutoCompleteCmd;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;

/**
 * @author: target
 * @date: 2019/5/24 18:09
 * @description:
 */
@Slf4j
public class AutoCommitListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("---------------自动任务提交--------------");
        new AutoCompleteCmd(delegateTask.getId(), delegateTask.getVariables(), "自动提交").execute(Context.getCommandContext());
    }
}
