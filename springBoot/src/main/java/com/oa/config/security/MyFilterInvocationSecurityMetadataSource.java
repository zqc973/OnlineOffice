package com.oa.config.security;

import com.oa.pojo.Menu;
import com.oa.pojo.Role;
import com.oa.service.system.MenuService;

import com.oa.service.system.RoleService;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @PackageName:com.oa.config.security
 * @ClassName:MyFilterInvocationSecurityMetadataSource
 * @Description: 获取当前访问资源所需要的角色
 * @Author: zqc
 * @date 2021/3/1 9:32
 */
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    // ant路径格式匹配器
    AntPathMatcher pathMatcher = new AntPathMatcher();

    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;
    /**
     * 当前访问资源所需要的角色
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //这里需要强转称FilterInvocation的原因是因为要获取请求的url。FilterInvocation中有对应的方法
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 查询到所有的菜单
        List<Menu> allMenu = menuService.getAllMenus();
        // 通过ant匹配，查看当前路径所需要的角色
        for (Menu menu : allMenu) {
            if (pathMatcher.match(menu.getHref(),requestUrl)) {
                List<Role> roles = roleService.getRolesByMenuId(menu.getId());
                //角色如果为空 任何人都可以访问
                String[] rolesStr = new String[roles.size()];

                for (int i = 0; i < roles.size(); i++) {
                    rolesStr[i] = roles.get(i).getName();
                }
                //返回当前路径所需要的角色
                return SecurityConfig.createList(rolesStr);
            }
        }
        // 如果没有匹配的话，让他登录即可访问,随便授予一个角色，不要给null，防止在别的地方空指针
        return SecurityConfig.createList("ROLE_login");

    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
