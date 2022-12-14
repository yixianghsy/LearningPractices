package com.tulingxueyuan.mall.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.AddCarDTO;
import com.tulingxueyuan.mall.dto.CartItemStockDTO;
import com.tulingxueyuan.mall.modules.oms.service.OmsCartItemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@Api(tags = "CarController",description = "购物车服务接口")
@RequestMapping("/car")
public class CarController {

    @Autowired
    OmsCartItemService cartItemService;

    /**
     *  .post("/cart/add", {
     *           productId: this.id,
     *           productSkuId: this.skuId,
     *           quantity: 1,
     *         })
     * @return
     */
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public CommonResult add(@RequestBody AddCarDTO addCarDTO){
        Boolean result=cartItemService.add(addCarDTO);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }

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
        List<CartItemStockDTO> list= cartItemService.getList();

        return CommonResult.success(list);
    }

    /**
     *  更新商品数量
     *  this.axios.post('/car/update/quantity',Qs.stringify({
     *             id:item.id,
     *             quantity:item.quantity   当前数量
     *           }),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
     */
    @RequestMapping(value="/update/quantity",method = RequestMethod.POST)
    public CommonResult updateQuantity(
            @RequestParam Long id,
            @RequestParam Integer quantity){
        Boolean result= cartItemService.updateQuantity(id,quantity);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }

    /**
     *  删除
     *  this.axios.post('/car/delete',Qs.stringify({
     *             ids:item.id
     *           }),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
     */
    @RequestMapping(value="/delete",method = RequestMethod.POST)
    public CommonResult delete(
            @RequestParam Long ids){
        Boolean result= cartItemService.delete(ids);
        if(result){
            return  CommonResult.success(result);
        }
        else {
            return  CommonResult.failed();
        }
    }
}
