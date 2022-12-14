package com.tulingxueyuan.mall.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.CartItemStockDTO;
import com.tulingxueyuan.mall.modules.oms.service.OmsCartItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "CarController",description = "购物车服务接口")
@RequestMapping("/car")
public class CarController {

    @Autowired
    OmsCartItemService cartItemService;
    /**
     *  初始化状态栏的购物车商品数量
     *   this.axios.get('/car/products/sum').then((res=0)=>{
     */
    @RequestMapping(value="/products/sum",method = RequestMethod.GET)
    public CommonResult getCarProdutSum(){
        Integer count= cartItemService.getCarProdutSum();
        return CommonResult.success(count);
    }
    /**
     * 获取购物数据初始化
     *  this.axios.get('/car/list')
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public CommonResult getList(){
        List<CartItemStockDTO> list = cartItemService.getList();
        return  CommonResult.success(list);
    }
}
