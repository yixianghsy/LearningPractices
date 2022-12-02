package com.tulingxueyuan.mall.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.modules.ums.model.UmsMemberReceiveAddress;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@Api(tags = "MemberAddressController",description = "收货地址服务接口")
@RequestMapping("/member/address")
public class MemberAddressController {

    @Autowired
    UmsMemberReceiveAddressService addressService;

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address){
        Boolean result= addressService.add(address);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }


    @RequestMapping(value="/update/{id}",method = RequestMethod.POST)
    public CommonResult edit(
            @PathVariable  Long id,
            @RequestBody UmsMemberReceiveAddress address){
        address.setId(id);
        Boolean result= addressService.edit(address);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }


    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id){
        Boolean result= addressService.delete(id);
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
