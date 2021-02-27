package com.oa.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @PackageName:com.oa.config.swagger
 * @ClassName:SwaggerConfig
 * @Description: Swagger配置类
 * @Author: zqc
 * @date 2021/2/26 9:18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oa.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchems());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("在线办公系统")
                .description("在线办公系统Api文档")
                .version("1.0")
                .contact(new Contact("周旗场", "http:localhost:8081/doc.html","1101570660@qq.com"))
                .build();
    }
    private List<SecurityScheme> securitySchems(){
        //设置请求头参数
        ApiKey apiKey = new ApiKey("Authorization", "Authorization","Header");
        return Collections.singletonList(apiKey);
    }

    private List<SecurityContext> securityContexts(){
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();

        AuthorizationScope authorizationScope = new AuthorizationScope( "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[ 1];
        authorizationScopes[ 0] = authorizationScope;
        result.add(new SecurityReference( "Authorization", authorizationScopes));
        return result;
    }
}
