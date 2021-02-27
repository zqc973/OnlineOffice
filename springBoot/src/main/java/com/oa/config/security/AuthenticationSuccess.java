package com.oa.config.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.oa.common.RespBean;
import com.oa.constant.JwtTokenConstant;
import com.oa.constant.RedisConstant;
import com.oa.utils.JwtTokenUtil;
import com.oa.utils.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.oa.config.security
 * @ClassName:AuthenticationSuccess
 * @Description: 登录成功处理器
 * @Author: zqc
 * @date 2021/2/22 14:46
 */
@Configuration
public class AuthenticationSuccess implements AuthenticationSuccessHandler {

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 登录成功后 处理器
     *  拿到用户信息
     *  根据用户信息生成token返给前端
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        //获取当前用户信息
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        //生成jwt
        String jwtToken = jwtTokenUtil.generateToken(principal);
        //String encryptToken = jwtTokenUtil.encryptToken(jwtToken);

        String refresh_token = IdUtil.simpleUUID();

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", jwtToken);
        map.put("refresh_token", refresh_token);

        redisUtil.set(refresh_token,jwtToken,60*30);

        RespBean resp = RespBean.success("登录成功", map);

        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(resp));
        writer.flush();
        writer.close();
    }
}
