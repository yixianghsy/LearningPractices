package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.order.dto.CartItemStockDTO;
import com.mall.order.model.OmsCartItem;
import com.mall.portal.domain.CartProduct;
import com.mall.portal.domain.CartPromotionItem;
import com.mall.portal.dto.AddCarDTO;
import com.mall.portal.service.OmsCartItemService;
import com.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理Controller
 * Created on 2018/8/2.
 */
@Controller
@Api(tags = "OmsCartItemController", description = "购物车管理")
@RequestMapping("/car")
public class OmsCartItemController {
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

//    @ApiOperation(value = "添加商品到购物车",notes = "杨过修改购物逻辑,数据不必全都从前台传")
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult add(@RequestBody OmsCartItem cartItem) {
//        int count = cartItemService.add(cartItem);
//        if(count > 0){
//            return CommonResult.success(cartItemService.cartItemCount());
//        }
//        return CommonResult.failed();
//    }
    /**
     *  .post("/cart/add", {
     *           productId: this.id,
     *           productSkuId: this.skuId,
     *           quantity: 1,
     *         })
     * @return
     */
    @ApiOperation(value = "添加商品到购物车",notes = "杨过修改购物逻辑,数据不必全都从前台传")
    @RequestMapping(value="/add",method = RequestMethod.POST)
    @ResponseBody
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
    @ResponseBody
    @RequestMapping(value="/products/sum",method = RequestMethod.GET)
    public CommonResult getCarProdutSum(){
        Integer count= cartItemService.getCarProdutSum();
        return CommonResult.success(count);
    }
//    @ApiOperation("获取某个会员的购物车列表")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<OmsCartItem>> list() {
//        // TODO  SQL 缺少字段
//        List<OmsCartItem> cartItemList = cartItemService.list(memberService.getCurrentMember().getId());
//        return CommonResult.success(cartItemList);
//    }
    /**
     * 获取购物数据初始化
     *  this.axios.get('/car/list')
     */
    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value="/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CartItemStockDTO>> getList(){
        List<CartItemStockDTO> list= cartItemService.getList(memberService.getCurrentMember().getId());

        return CommonResult.success(list);
    }
    @ApiOperation("获取某个会员的购物车列表,包括促销信息")
    @RequestMapping(value = "/list/promotion", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CartPromotionItem>> listPromotion() {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId());
        return CommonResult.success(cartPromotionItemList);
    }

    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/update/quantity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateQuantity(@RequestParam Long id,
                                       @RequestParam Integer quantity) {
        int count = cartItemService.updateQuantity(id, memberService.getCurrentMember().getId(), quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取购物车中某个商品的规格,用于重选规格")
    @RequestMapping(value = "/getProduct/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CartProduct> getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = cartItemService.getCartProduct(productId);
        return CommonResult.success(cartProduct);
    }

    @ApiOperation("修改购物车中商品的规格")
    @RequestMapping(value = "/update/attr", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateAttr(@RequestBody OmsCartItem cartItem) {
        int count = cartItemService.updateAttr(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除购物车中的某个商品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = cartItemService.delete(memberService.getCurrentMember().getId(), ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("清空购物车")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult clear() {
        int count = cartItemService.clear(memberService.getCurrentMember().getId());
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}