package com.oa.service.organization;

import com.oa.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
public interface UserService extends IService<User> {

    List<User> getAllUsers(User user);

    int addUser(User user);
}
