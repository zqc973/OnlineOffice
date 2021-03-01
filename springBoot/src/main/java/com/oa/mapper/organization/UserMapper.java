package com.oa.mapper.organization;

import com.oa.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
public interface UserMapper extends BaseMapper<User> {


    User loadUserByUsername(String username);


    List<User> getAllUsers(User user);
}
