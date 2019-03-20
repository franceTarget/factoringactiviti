package com.servingcloud.factoring.vanke.models.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("发布请求")
@Data
public class DeployReq {
    private String modleKey;
    private String name;

}
