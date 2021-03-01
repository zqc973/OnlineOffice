package com.oa.config.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @PackageName:com.oa.config.security
 * @ClassName:MyAcccessDecisionManager
 * @Description: 决策管理器
 * @Author: zqc
 * @date 2021/3/1 9:32
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {


    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        for (ConfigAttribute configAttribute : configAttributes) {
            // 判断是否只需登录即可访问的角色
            if("ROLE_login".equals(configAttribute.getAttribute())){
                //判断是否登录
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录，请登录！");
                }
                return;
            }

            // 当前用户所具有的角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if(authorities.contains(new SimpleGrantedAuthority("ROLE_super_admin"))){
                return;
            }
            //访问的资源的角色和用户的角色进行比对
            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(configAttribute.getAttribute())){
                    return;
                }
            }

        }
        // 如果有权限的话，就不会走到这，直接抛出异常
        throw new AccessDeniedException("权限不足，请联系管理员！");
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
