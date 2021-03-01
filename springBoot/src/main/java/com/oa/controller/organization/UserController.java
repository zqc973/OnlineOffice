package com.oa.controller.organization;


import cn.hutool.core.util.StrUtil;
import com.oa.common.RespBean;
import com.oa.pojo.User;
import com.oa.service.organization.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


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
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "trueName", value = "真实姓名", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "phoneNum", value = "手机号码", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "页面数量", required = false, dataType = "String", paramType = "query"),
    })
    public RespBean getAllUsers(User user){
        List<User> users = userService.getAllUsers(user);

        return RespBean.success("成功", users);
    }

    @PostMapping("/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "trueName", value = "真实姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phoneNum", value = "手机号码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "联系地址", required = false, dataType = "String", paramType = "query")
    })
    public RespBean addUser(User user){
        if(StrUtil.isBlank(StrUtil.utf8Str(user.getUsername()))){
            return RespBean.error("用户名不能为空");
        }
        if(StrUtil.isBlank(StrUtil.utf8Str(user.getTrueName()))){
            return RespBean.error("真实姓名不能为空");
        }
        if(StrUtil.isBlank(StrUtil.utf8Str(user.getPhoneNum()))){
            return RespBean.error("手机号码不能为空");
        }
        if(StrUtil.isBlank(StrUtil.utf8Str(user.getSex()))){
            user.setSex(1);
        }

        int count = userService.addUser(user);
        if(count >= 1){
            return RespBean.success("添加成功！", true);
        }
        return RespBean.error("添加失败", false);
    }

}
