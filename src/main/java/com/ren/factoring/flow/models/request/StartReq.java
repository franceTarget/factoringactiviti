package com.ren.factoring.flow.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("启动流程")
public class StartReq {
    @ApiModelProperty("流程部署ID")
    private String procDefId;
    @ApiModelProperty("流程定义的key")
    private String procDefkey;
    @ApiModelProperty("业务ID")
    private String businessKey;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("发起人")
    private String startActId;
}
