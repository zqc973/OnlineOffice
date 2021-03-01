package com.oa.service.system;

import com.oa.pojo.Role;
import com.oa.mapper.system.RoleMapper;
import com.oa.service.system.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zqc
 * @since 2021-02-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    public List<Role> getRolesByMenuId(Integer menuId){
        return roleMapper.getRolesByMenuId(menuId);
    }
}
