package com.oa.mapper.system;

import com.oa.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from t_role tr\n" +
            "            where tr.id in (\n" +
            "                select trm.role_id \n" +
            "                from t_role_menu trm \n" +
            "                where trm.menu_id = #{menuId}\n" +
            "                )")
    List<Role> getRolesByMenuId(Integer menuId);
}
