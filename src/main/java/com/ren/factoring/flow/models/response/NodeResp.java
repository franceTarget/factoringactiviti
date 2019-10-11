package com.ren.factoring.flow.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: target
 * @date: 2019/10/11 10:23
 * @description:
 */
@ApiModel("流程节点信息响应")
@Data
public class NodeResp {

    @ApiModelProperty("当前节点")
    private String preNode;

    @ApiModelProperty("下个节点")
    private String nextNode;

    @ApiModelProperty("el条件")
    private String el;

    @ApiModelProperty("当前节点类型")
    private String type;
}
