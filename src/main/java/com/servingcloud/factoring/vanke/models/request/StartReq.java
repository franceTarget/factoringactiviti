package com.servingcloud.factoring.vanke.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("启动流程")
public class StartReq {
    @ApiModelProperty("部署ID")
    private String deploymentId;
    @ApiModelProperty("业务ID")
    private String businessId;
    @ApiModelProperty("商户ID")
    private String tenantId;
}
