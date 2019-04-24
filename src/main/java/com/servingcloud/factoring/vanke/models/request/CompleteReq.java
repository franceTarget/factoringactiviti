package com.servingcloud.factoring.vanke.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("任务提交请求")
public class CompleteReq {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程实例ID")
    private String processId;

    @ApiModelProperty("结果CODE 1.同意，0.否决")
    private String agree;

    @ApiModelProperty("内容")
    private String context;

}
