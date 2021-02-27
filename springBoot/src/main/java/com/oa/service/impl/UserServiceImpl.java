package com.oa.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oa.exception.MyAuthenticationException;
import com.oa.pojo.User;
import com.oa.mapper.UserMapper;
import com.oa.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Resource
    private UserMapper userMapper;

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StrUtil.isEmpty(username)) {
            return null;
        }
        // 根据用户名查询数据库
        User user = userMapper.loadUserByUsername(username);
        //if(ObjectUtil.isEmpty(user)){
        //    throw new UsernameNotFoundException("用户名不存在，登录失败！");
        //}
        //if (ObjectUtils.isEmpty(sysUser)) { return null; }// 查询权限
       //List<String> auths = roleMapper.selectOne(new QueryWrapper<User>().eq());
       // if (!CollectionUtils.isEmpty(auths)) {
       //     // 设置权限
       //     sysUser.setAuthorities(auths);
       // }
        return user;
    }
}
