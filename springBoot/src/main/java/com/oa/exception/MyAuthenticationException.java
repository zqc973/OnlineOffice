package com.oa.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @PackageName:com.oa.exception
 * @ClassName:MyAuthenticationException
 * @Description: 自定义登录失败处理器
 * @Author: zqc
 * @date 2021/2/25 21:57
 */
public class MyAuthenticationException extends AuthenticationException {


    public MyAuthenticationException(String msg) {
        super(msg);
    }
}
