package com.oa.service.system;

import com.oa.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
public interface RoleService extends IService<Role> {

    List<Role> getRolesByMenuId(Integer menuId);
}
