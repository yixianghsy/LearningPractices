package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.portal.service.UmsMemberReceiveAddressService;
import com.mall.sso.model.UmsMemberReceiveAddress;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "MemberAddressController",description = "收货地址服务接口")
@RequestMapping("/member/address")
public class MemberAddressController {
    @Autowired
    UmsMemberReceiveAddressService addressService;
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address){
        Boolean result= addressService.add(address)>0;
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }


    @RequestMapping(value="/update/{id}",method = RequestMethod.POST)
    public CommonResult edit(
            @PathVariable Long id,
            @RequestBody UmsMemberReceiveAddress address){
        address.setId(id);
        boolean result = addressService.update(id, address) > 0;
//        Boolean result= addressService.edit(address);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }


    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id){
        Boolean result= addressService.delete(id)>0;
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }

    ///member/address/list
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public CommonResult list(){
        List<UmsMemberReceiveAddress> list = addressService.listByMemberId();
        return CommonResult.success(list);
    }
}
