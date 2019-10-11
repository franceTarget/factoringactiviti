package com.ren.factoring.flow.config;

import com.ren.factoring.flow.models.response.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2 {//http://localhost:8700/swagger-ui.html

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)//是否开启api  可以配置文件中
                .groupName("1rest")
                .genericModelSubstitutes(Response.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .apiInfo(restApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ren.factoring.flow.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo restApiInfo() {
        StringBuilder descBuilder = new StringBuilder();
        descBuilder.append("<h4><strong>activiti-service的api:</strong></h4>");

        return new ApiInfoBuilder()
                .title("流程管理")
                .description(descBuilder.toString())
                //.termsOfServiceUrl("http://www.test/")
                //.contact(new Contact("rest", "", ""))
                .version("1.0")
                .build();
    }

}