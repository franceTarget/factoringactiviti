package com.ren.factoring.flow.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: target
 * @date: 2019/9/11 15:08
 * @description:
 */
@Data
@ApiModel("任务提交基础请求实体")
public class CompleteBaseReq implements Serializable {

    private static final long serialVersionUID = -4079893709028370235L;

    @ApiModelProperty("用户id")
    private String userId;

    @NotNull(message = "任务id不能为空")
    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程实例ID")
    private String processId;

    @ApiModelProperty("分支条件")
    private String branchCondition;

    @ApiModelProperty("审批意见")
    private String comment;
}
