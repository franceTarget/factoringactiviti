package com.ren.factoring.flow.models.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: target
 * @date: 2019/6/11 15:07
 * @description:
 */

@Data
public class ActRuTask implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    private Integer rev;

    private String executionId;

    private String procInstId;

    private String procDefId;

    private String name;

    private String parentTaskId;

    private String description;

    private String taskDefKey;

    private String owner;

    private String assignee;

    private String delegation;

    private Integer priority;

    private Date createTime;

    private Date dueDate;

    private String category;

    private Integer suspensionState;

    private String tenantId;

    private String formKey;
}
