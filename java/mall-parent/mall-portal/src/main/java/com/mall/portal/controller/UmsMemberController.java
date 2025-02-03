package com.mall.portal.controller;
import com.mall.api.CommonResult;
import com.mall.portal.domain.MemberDetails;
import com.mall.sso.model.UmsAdmin;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsRole;
import com.mall.sso.service.UmsMemberService;
import com.mall.util.JwtTokenUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * Created on 2018/8/3.
 */
@RestController
@RequestMapping("/user")
public class UmsMemberController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Reference
    private UmsMemberService memberService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsMember> register(@Validated @RequestBody  UmsMember umsMemberParam) {
        UmsMember umsAdmin = memberService.register(umsMemberParam);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        UmsMember login = memberService.login(username, password);
        if (login == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        String token = jwtTokenUtil.generateUserNameStr(login.getUsername());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        tokenMap.put("tokenHeader",tokenHeader);
        return CommonResult.success(tokenMap);
    }


//    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult getAuthCode(@RequestParam String telephone) {
//        return memberService.generateAuthCode(telephone);
//    }
//
//
//    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult updatePassword(@RequestParam String telephone,
//                                 @RequestParam String password,
//                                 @RequestParam String authCode) {
//        return memberService.updatePassword(telephone,password,authCode);
//    }
//
//
//    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult refreshToken(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader);
//        String refreshToken = memberService.refreshToken(token);
//        if (refreshToken == null) {
//            return CommonResult.failed("token已经过期！");
//        }
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", refreshToken);
//        tokenMap.put("tokenHead", tokenHead);
//        return CommonResult.success(tokenMap);
//    }
}
