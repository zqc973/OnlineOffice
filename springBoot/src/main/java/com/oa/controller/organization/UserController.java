package com.oa.controller.organization;


import com.oa.common.RespBean;
import com.oa.pojo.User;
import com.oa.service.organization.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @PackageName:com.oa.controller.organization
 * @ClassName:UserController
 * @Description: 用户控制器
 * @Author: zqc
 * @date 2021/3/1 9:32
 */
@Api(tags = "用户管理")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("获取所有用户")
    @GetMapping("/users")
    public RespBean getAllUsers(){
        List<User> users = userService.getAllUsers(new User());

        return RespBean.success("成功", users);
    }

}
