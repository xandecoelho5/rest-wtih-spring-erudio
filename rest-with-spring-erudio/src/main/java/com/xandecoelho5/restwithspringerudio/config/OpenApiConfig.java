package com.xandecoelho5.restwithspringerudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title("RESTful API with JAVA 19 and Spring Boot 3")
                .version("v1")
                .description("Restful API with Spring Boot 3.0")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org")
                );
    }
}
