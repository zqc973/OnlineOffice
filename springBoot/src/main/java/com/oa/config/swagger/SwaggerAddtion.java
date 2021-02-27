package com.oa.config.swagger;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Consumer;

/**
 * 手动添加swagger接口，如登录接口等
 *
 * @author songyinyin
 * @date 2020/6/17 下午 10:03
 */
@Component
public class SwaggerAddtion implements ApiListingScannerPlugin {
    /**
     * Implement this method to manually add ApiDescriptions
     * 实现此方法可手动添加ApiDescriptions
     *
     * @param context - Documentation context that can be used infer documentation context
     * @return List of {@link ApiDescription}
     * @see ApiDescription
     */
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {


        ApiDescription loginApiDescription = new ApiDescription("default", "/login", "登录模块","登录接口",
                Collections.singletonList(loginOperation()), false);

        ApiDescription logoutApiDescription = new ApiDescription("default", "/logout", "登录模块","登录接口",
                Collections.singletonList(logoutOperation()), false);

        return Arrays.asList(loginApiDescription,logoutApiDescription);
    }

    private Operation loginOperation(){
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户登录")
                .notes("用户登录")
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) // 接收参数格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE)) // 返回参数格式
                .tags(Sets.newHashSet("登录模块"))
                .requestParameters(Arrays.asList(
                        new RequestParameterBuilder()
                                .description("用户名")
                                .name("username")
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("密码")
                                .name("password")
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("验证码唯一标识")
                                .name("captchaKey")
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("验证码")
                                .name("captcha")
                                .in(ParameterType.QUERY)
                                .required(true)
                                .build()
                ))
                .build();
    };
    private Operation logoutOperation(){
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户登出")
                .notes("用户登出")
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) // 接收参数格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE)) // 返回参数格式
                .tags(Sets.newHashSet("登录模块"))
                .requestParameters(Collections.singletonList(
                        new RequestParameterBuilder()
                                .description("Authorization")
                                .name("Authorization")
                                .in(ParameterType.HEADER)
                                .required(true)
                                .build()
                ))
                .build();
    };

    /**
     * 是否使用此插件
     * 
     * @param documentationType swagger文档类型
     * @return true 启用
     */
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
