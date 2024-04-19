package com.mall.order.controller;

import com.mall.api.CommonResult;
import com.mall.order.model.OmsOrderSetting;
import com.mall.order.service.OmsOrderSettingService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 订单设置管理Controller
 * Created by macro on 2018/10/16.
 */
@Controller
@RequestMapping("/orderSetting")
public class OmsOrderSettingController {
    @Reference
    private OmsOrderSettingService orderSettingService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
