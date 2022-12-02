package com.tulingxueyuan.mall.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.common.util.ComConstants;
import com.tulingxueyuan.mall.modules.ums.model.UmsMember;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Controller
@Api(tags = "UserController", description = "前台用户服务接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    UmsMemberService memberService;

    @Autowired
    HttpSession session;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsMember> register(@Validated @RequestBody UmsMember umsMemberParam) {
        UmsMember umsAdmin = memberService.register(umsMemberParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@Validated UmsMember umsMemberParam) {
        UmsMember login = memberService.login(umsMemberParam.getUsername(), umsMemberParam.getPassword());
        if (login == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        session.setAttribute(ComConstants.FLAG_CURRENT_USER,login);
        System.out.println(session.getId());
        Map<String,String> tokenMap = new HashMap<>();
        //jwt
        return  CommonResult.success(tokenMap);
    }
}
