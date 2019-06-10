package com.ren.factoring.vanke.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:activiti-boot.xml"})
public class ActivitiConfig {
}
