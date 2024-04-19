package com.mall.order.controller;


import com.mall.api.CommonResult;
import com.mall.order.model.OmsCompanyAddress;
import com.mall.order.service.OmsCompanyAddressService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 收货地址管理Controller
 * Created by macro on 2018/10/18.
 */
@Controller

@RequestMapping("/companyAddress")
public class OmsCompanyAddressController {
    @Reference
    private OmsCompanyAddressService companyAddressService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
