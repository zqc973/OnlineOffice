package com.oa.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.oa.common.RespBean;
import com.oa.constant.JwtTokenConstant;
import com.oa.exception.MyAuthenticationException;
import com.oa.utils.JwtTokenUtil;
import com.oa.utils.RedisUtil;
import com.oa.vo.UserVo;
import io.swagger.annotations.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtTokenUtil jwtTokenUtil;


    @ApiOperation(value = "生成验证码")
    @GetMapping("/captcha")
    public RespBean getCaptcha(){
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String imageBase64 = circleCaptcha.getImageBase64();
        String captchaKey = IdUtil.simpleUUID();

        redisUtil.set(captchaKey, circleCaptcha.getCode(), 60);

        Map<String, Object> data = new HashMap<>();
        data.put("imageBase64", imageBase64);
        data.put("captchaKey", captchaKey);

        return RespBean.success("获取验证码成功", data);
    }

    @ApiOperation(value = "刷新Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refreshToken", value = "刷新令牌", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/refreshToken")
    public RespBean refreshToken(@RequestHeader(JwtTokenConstant.JWT_TOKEN_HEADER) String headerToken, String refreshToken){
        if(StrUtil.isEmpty(refreshToken) || StrUtil.isEmpty(headerToken) || headerToken.startsWith(JwtTokenConstant.JWT_TOKEN_HEAD)) {
            return RespBean.error("身份凭证无效，请重新登录！");
        }
        String redis_token = StrUtil.utf8Str(redisUtil.get(headerToken));

        if(jwtTokenUtil.isTokenExpiration(refreshToken)
                && !redisUtil.hasKey(refreshToken)
                && refreshToken.equals(redis_token)){
            return RespBean.error("身份认证已过期，请重新登录！");
        }

        Map<String, Object> data = jwtTokenUtil.refreshToken(refreshToken);
        String refresh_token = StrUtil.utf8Str(data.get("refresh_token"));
        String access_token = StrUtil.utf8Str(data.get("access_token"));

        redisUtil.del(refreshToken);
        redisUtil.set(JwtTokenConstant.JWT_TOKEN_HEAD+access_token, refresh_token, JwtTokenConstant.REFRESH_KEY_EXPIRATION);
        redisUtil.set(access_token, access_token, JwtTokenConstant.CLAIM_KEY_EXPIRATION);

        return RespBean.success("令牌刷新成功", data);
    }
}
