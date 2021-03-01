package com.oa.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName:com.oa.controller
 * @ClassName:TestController
 * @Description:
 * @Author: zqc
 * @date 2021/3/1 15:15
 */
@Api(tags = "测试模块")
@RestController
public class TestController {

    @ApiOperation("测试1")
    @RequestMapping("/authority")
    public String test1(){
        return "test1";
    }
    @ApiOperation("测试2")
    @RequestMapping("/administrative")
    public String test2(){
        return "test1";
    }

    @ApiOperation("测试3")
    @RequestMapping("/archive")
    public String test3(){
        return "test1";
    }
}
