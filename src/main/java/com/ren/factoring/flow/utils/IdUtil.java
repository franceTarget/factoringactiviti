package com.ren.factoring.flow.utils;

public class IdUtil {

    public static Long newId() {
        SnowflakeIdWorker idWorker = SnowflakeIdWorker.getInstance();
        return idWorker.nextId();
    }
}
