package com.oa.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.util.StrUtil;
import com.oa.common.RespBean;
import com.oa.exception.MyAuthenticationException;
import com.oa.vo.UserVo;
import io.swagger.annotations.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @PackageName:com.oa.controller
 * @ClassName:LoginController
 * @Description: 登录控制器
 * @Author: zqc
 * @date 2021/2/20 15:37
 */
@Api(tags = "登录模块")
@RestController
public class LoginController {

    @Resource
    private UserDetailsService userDetailsService;

    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/loginsdg")
    public RespBean login(UserVo userVo, HttpServletRequest request){
        String code = userVo.getCode();
        String username = userVo.getUsername();
        String password = userVo.getPassword();

        if(StrUtil.isBlank(username)){
            throw new MyAuthenticationException("登录失败，用户名不能为空！");
        }
        if(StrUtil.isBlank(password)){
            throw new MyAuthenticationException("登录失败，密码不能为空！");
        }
        if(StrUtil.isBlank(code)){
           // throw new MyAuthenticationException("登录失败，验证码不能为空！");
            return RespBean.error("登录失败，验证码不能为空！");
        }

        if("1234".equalsIgnoreCase(code)){
            throw new MyAuthenticationException("登录失败，验证码输入不正确！");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return RespBean.success("登陆成功");
    }

    @GetMapping("/getRomCode")
    public String getRomCode(){

        return CaptchaUtil.createCircleCaptcha(200, 100, 4, 20).getCode();
    }
}
