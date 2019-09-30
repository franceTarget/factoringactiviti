package com.ren.factoring.flow.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发布请求")
@Data
public class DeployReq {
    @ApiModelProperty("创建模型的key值")
    private String modleKey;

    @ApiModelProperty("发布流程的名称")
    private String name;

    @ApiModelProperty("商户id")
    private String tenantId;

}
