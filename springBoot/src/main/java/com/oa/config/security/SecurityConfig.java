package com.oa.config.security;

import cn.hutool.json.JSONUtil;
import com.oa.common.RespBean;
import com.oa.constant.JwtTokenConstant;
import com.oa.filter.JwtTokenFilter;
import com.oa.service.organization.UserServiceImpl;
import com.oa.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * @PackageName:com.oa.config
 * @ClassName:SecurityConfig
 * @Description: Security配置类
 * @Author: zqc
 * @date 2021/2/20 16:10
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private MyAccessDecisionManager myAccessDecisionManager;
    @Resource
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
    @Resource
    private MyAuthenticationSuccessHandler authenticationSuccess;
    @Resource
    private MyAuthenticationFailureHandler authenticationFailureHandler;
    @Resource
    private UserServiceImpl userDetailsService;
    @Resource
    private JwtTokenFilter jwtTokenFilter;
    @Resource
    private RedisUtil redisUtil;


    /**
     * 密码加密编码器
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义哪些路径不需要经过过滤器链
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/captcha",
                        "/refreshToken",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**");
    }

    /**
     * 自定义用户从哪获取
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    /**
     * 登录认证逻辑
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                //403访问权限不足处理器 返回json
                .accessDeniedHandler(accessDeniedHandler())
                //未登录处理器
                .authenticationEntryPoint(authenticationEntryPoint())
            .and()
                //在用户名密码拦截器之前添加jwtToken拦截器
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //表单登录
                .formLogin()
                //登录成功处理器
                .successHandler(authenticationSuccess)
                //登录失败处理器
                .failureHandler(authenticationFailureHandler)
                .loginProcessingUrl("/login")
                .permitAll()
            .and()
                .logout()
                //退出登录处理器
                .logoutSuccessHandler(logoutSuccessHandler())
                .logoutUrl("/logout")
                .permitAll()
            .and()
                //其余所有请求都需要登录认证才能访问
                .authorizeRequests()

                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //设置认证决策器
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        return o;
                    }
                })
                .anyRequest().authenticated()
            .and()
                //关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                //禁用缓存
                .headers().cacheControl();
    }

    /**
     * 未登录处理器
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return ( request,  response,  authException) -> {
            response.setContentType("application/json;charset=utf-8");

            RespBean respBean = RespBean.error("您还未登录，请登录后再试！").setCode(401);
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(respBean));
            writer.flush();
            writer.close();
        };
    }

    /**
     * 访问权限不足处理器
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=utf-8");

            RespBean respBean = new RespBean(403, "权限不足，请联系管理员！", null);
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(respBean));
            writer.flush();
            writer.close();
        };
    }

    /**
     * 登出处理器
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return (request, response, authentication) -> {
            String header_token = request.getHeader(JwtTokenConstant.JWT_TOKEN_HEADER);
            String access_token = header_token.replace(JwtTokenConstant.JWT_TOKEN_HEAD, "");

            redisUtil.del(access_token,header_token);

            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(RespBean.success("退出登录成功！")));
            writer.flush();
            writer.close();
        };
    }
}
