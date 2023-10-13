//package com.mall.sso.controller;
//
//import com.mall.api.CommonResult;
//import com.mall.sso.dto.UmsAdminLoginParam;
//import com.mall.sso.model.UmsAdmin;
//import com.mall.sso.service.UmsAdminService;
//import com.mall.sso.service.UmsRoleService;
//import com.mall.utils.ComConstants;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpSession;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@Api(tags = "UmsAdminController", description = "后台用户管理")
//@RequestMapping("/admin")
//public class UmsAdminController2 {
//    @Autowired
//    HttpSession session;
//    @Reference
//    private UmsAdminService adminService;
//    @Reference
//    private UmsRoleService roleService;
//    @ApiOperation(value = "登录")
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
//         UmsAdmin login = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
//        if (login == null) {
//            return CommonResult.validateFailed("用户名或密码错误");
//        }
//        session.setAttribute(ComConstants.FLAG_CURRENT_USER,login);
//        System.out.println(session.getId());
//        Map<String, String> tokenMap = new HashMap<>();
//        // jwt
//        return CommonResult.success(tokenMap);
//
//
//    }
//}
