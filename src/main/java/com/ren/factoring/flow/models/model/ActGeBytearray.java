package com.ren.factoring.flow.models.model;

import lombok.Data;

@Data
public class ActGeBytearray {

    private String id;

    private Integer rev;

    private String name;

    private String deploymentId;

    private byte[] bytes;

    private Integer generated_;
}
