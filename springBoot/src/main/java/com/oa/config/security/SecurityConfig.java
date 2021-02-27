package com.oa.config.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oa.common.RespBean;
import com.oa.exception.MyAuthenticationException;
import com.oa.filter.JwtTokenFilter;
import com.oa.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
    private AuthenticationSuccess authenticationSuccess;
    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Resource
    private UserServiceImpl userDetailsService;
    @Resource
    private JwtTokenFilter jwtTokenFilter;


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
        auth.userDetailsService(userDetailsService);
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
            .and()
                //其余所有请求都需要登录认证才能访问
                .authorizeRequests().anyRequest().authenticated()
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
}
