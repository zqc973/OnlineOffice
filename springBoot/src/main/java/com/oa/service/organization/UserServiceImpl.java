package com.oa.service.organization;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oa.pojo.User;
import com.oa.mapper.organization.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
        if(ObjectUtil.isEmpty(user)){
            throw new UsernameNotFoundException("用户名不存在，登录失败！");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers(User user) {
        return userMapper.getAllUsers(user);
    }

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }
}
