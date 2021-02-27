package com.oa.mapper;

import com.oa.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from t_user ")
    User loadUserByUsername(String username);
}
