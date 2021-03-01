package com.oa.service.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.oa.constant.RedisConstant;
import com.oa.pojo.Menu;
import com.oa.mapper.system.MenuMapper;
import com.oa.service.system.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        if(redisUtil.hasKey(RedisConstant.SYS_MENU)){
            menus = JSONUtil.toList(StrUtil.utf8Str(redisUtil.get(RedisConstant.SYS_MENU)), Menu.class);
        }
        if(menus == null || menus.size() == 0){
            menus = menuMapper.getAllMenus();
            redisUtil.set(RedisConstant.SYS_MENU, JSONUtil.toJsonStr(menus));
        }
        return menus;
    }
}
