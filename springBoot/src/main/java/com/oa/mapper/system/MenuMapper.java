package com.oa.mapper.system;

import com.oa.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select * from t_menu")
    List<Menu> getAllMenus();
}
