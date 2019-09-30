package com.ren.factoring.flow.config;

import com.ren.factoring.flow.utils.SnowflakeIdWorker;
import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * @author: target
 * @date: 2019/6/10 9:27
 * @description:改变activiti的id生成策略
 */
public class SnowflakeIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        SnowflakeIdWorker idWorker = SnowflakeIdWorker.getInstance();
        return idWorker.nextId() + "";
    }
}
