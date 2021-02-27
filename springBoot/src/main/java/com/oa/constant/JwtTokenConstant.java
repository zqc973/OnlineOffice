package com.oa.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @PackageName:com.oa.constant
 * @ClassName:JwtTokenConstant
 * @Description: JwtToken常量
 * @Author: zqc
 * @date 2021/2/23 9:45
 */

public class JwtTokenConstant {
    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "iat";
    public static final String CLAIM_KEY_AUTHORITIES = "auth";

    public static  String CLAIM_KEY_SECRET = "OnlineOfficeByZqc";
    public static  String JWT_TOKEN_SECRET = "OnlineOfficeJwtT";
    //@Value("${jwt.tokenHeader}")
    public static  String JWT_TOKEN_HEADER = "Authorization";
    //@Value("${jwt.tokenHead}")
    public static  String JWT_TOKEN_HEAD = "Bearer ";
    public static final long CLAIM_KEY_EXPIRATION = 60 * 5;
}
