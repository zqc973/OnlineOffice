package com.oa.config.security;

import cn.hutool.json.JSONUtil;
import com.oa.common.RespBean;
import com.oa.exception.MyAuthenticationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @PackageName:com.oa.config.security
 * @ClassName:MyAuthenticationFailureHandler
 * @Description: 登录失败处理器
 * @Author: zqc
 * @date 2021/2/26 15:32
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        //根据异常信息判断哪种登录失败
        String msg = "";
        if(authenticationException instanceof MyAuthenticationException){
            msg = authenticationException.getMessage();
        }else if(authenticationException instanceof LockedException){
            msg = "账号被锁定，登录失败！";
        }else if(authenticationException instanceof BadCredentialsException){
            msg = "用户名或密码错误，登录失败！";
        }else if(authenticationException instanceof DisabledException){
            msg = "账号被禁用，登录失败！";
        }else if(authenticationException instanceof AccountExpiredException){
            msg = "账号已过期，登录失败！";
        }else if(authenticationException instanceof CredentialsExpiredException){
            msg = "密码已过期，登录失败！";
        }else if(authenticationException instanceof InternalAuthenticationServiceException){
            msg = "用户名不存在，登录失败！";
        }else {
            msg = "登录失败！";
        }

        RespBean respBean = RespBean.error(msg).setCode(401);
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(respBean));
        writer.flush();
        writer.close();
    }
}
