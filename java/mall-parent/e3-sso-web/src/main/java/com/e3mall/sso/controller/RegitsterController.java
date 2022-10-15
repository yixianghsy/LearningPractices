package com.e3mall.sso.controller;


import com.e3mall.mapper.pojo.TbUser;
import com.e3mall.sso.service.RegisterService;
import com.e3mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能Controller
 * <p>Title: RegitsterController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class RegitsterController {
    @Reference
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
        E3Result e3Result = registerService.checkData(param, type);
        return e3Result;
    }

    @RequestMapping(value="/user/register", method= RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user) {
        E3Result e3Result = registerService.register(user);
        return e3Result;
    }
}
