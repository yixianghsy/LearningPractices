package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.service.UmsMemberReceiveAddressService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/member/address")
public class MemberAddressController {

    @Reference
    UmsMemberReceiveAddressService memberReceiveAddressService;

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address){
        Boolean result= memberReceiveAddressService.add(address);
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
        Boolean result= memberReceiveAddressService.edit(address);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }


    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id){
        Boolean result= memberReceiveAddressService.delete(id);
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
        List<UmsMemberReceiveAddress> list = memberReceiveAddressService.listByMemberId();
        return CommonResult.success(list);
    }
}
