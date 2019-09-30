package com.ren.factoring.flow.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: target
 * @date: 2019/5/13 14:20
 * @description:
 */
@Slf4j
public class UserTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("---------------用户任务监听--------------");
        delegateTask.addCandidateGroup("admin");
    }
}
