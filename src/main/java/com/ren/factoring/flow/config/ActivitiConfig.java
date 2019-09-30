package com.ren.factoring.flow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:activiti-boot.xml"})
public class ActivitiConfig {
}
