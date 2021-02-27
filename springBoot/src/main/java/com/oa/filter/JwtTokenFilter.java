package com.oa.filter;

import cn.hutool.core.util.StrUtil;
import com.oa.constant.JwtTokenConstant;
import com.oa.exception.MyAuthenticationException;
import com.oa.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PackageName:com.oa.filter
 * @ClassName:JwtTokenFilter
 * @Description: JwtToken拦截器
 * @Author: zqc
 * @date 2021/2/23 9:37
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if("a".equals("a")){
            try {
                throw new MyAuthenticationException("llllllll");
            }catch (AuthenticationException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        String authorization = request.getHeader(JwtTokenConstant.JWT_TOKEN_HEADER);
        if(StrUtil.isNotEmpty(authorization) && authorization.startsWith(JwtTokenConstant.JWT_TOKEN_HEAD)) {
            String jwt = authorization.replace(JwtTokenConstant.JWT_TOKEN_HEAD, "");

            String username = jwtTokenUtil.getUserNameByToken(jwt);
            //获取权限
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //将解析的用户放入Security上下文中
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
