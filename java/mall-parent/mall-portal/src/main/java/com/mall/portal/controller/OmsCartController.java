package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.api.ResultCode;
import com.mall.exception.Asserts;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.service.PmsProductService;
import com.mall.mansger.service.PmsSkuStockService;
import com.mall.order.dto.AddCarDTO;
import com.mall.order.model.OmsCartItem;
import com.mall.order.service.OmsCartItemService;
import com.mall.portal.domain.MemberDetails;
import com.mall.sso.model.UmsMember;
import com.mall.sso.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理Controller
 * Created on 2018/8/2.
 */
@RestController
@RequestMapping("/car")
public class OmsCartController {
    @Reference
    private OmsCartItemService cartItemService;
    @Reference
    private UmsMemberService memberService;

    @Reference
    private  PmsSkuStockService skuStockService;

    @Reference
    private PmsProductService productService;
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
        // TODO 这里获取当前用户id
        UmsMember currentMember = memberService.getCurrentMember();
        // 查询sku
        PmsSkuStock sku = skuStockService.getById(addCarDTO.getProductSkuId());
        if (sku == null) Asserts.fail(ResultCode.VALIDATE_FAILED);
        addCarDTO.setSp1(sku.getSp1());
        addCarDTO.setSp2(sku.getSp2());
        addCarDTO.setSp3(sku.getSp3());
        addCarDTO.setPic(sku.getPic());
        addCarDTO.setSkuCode(sku.getSkuCode());
        addCarDTO.setMemberId(currentMember.getId());
        PmsProduct product = productService.getById(addCarDTO.getProductId());
        if (product == null) Asserts.fail(ResultCode.VALIDATE_FAILED);
        addCarDTO.setProductName(product.getName());
        addCarDTO.setProductBrand(product.getBrandName());
        addCarDTO.setProductSn(product.getProductSn());
        addCarDTO.setProductSubTitle(product.getSubTitle());
        addCarDTO.setProductCategoryId(product.getProductCategoryId());
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
        // 标识
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails =(MemberDetails) authentication.getPrincipal();
        Long memberId =   memberDetails.getUmsMember().getId();
        Integer count= cartItemService.getCarProdutSum(memberId);
        return CommonResult.success(count);
    }
    /**
     * 获取购物数据初始化
     *  this.axios.get('/car/list')
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OmsCartItem>> list() {

        // 标识
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails =(MemberDetails) authentication.getPrincipal();
        UmsMember umsMember = memberDetails.getUmsMember();
        // TODO  SQL 缺少字段
        List<OmsCartItem> cartItemList = cartItemService.list(memberDetails.getUmsMember().getId());
        return CommonResult.success(cartItemList);
    }
    /**
     * 获取购物数据初始化
     *  this.axios.get('/car/list')
     */

//    @RequestMapping(value="/list",method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<CartItemStockDTO>> getList(){
//        List<CartItemStockDTO> list= cartItemService.getList(memberService.getCurrentMember().getId());
//
//        return CommonResult.success(list);
//    }
//    @ApiOperation("获取某个会员的购物车列表,包括促销信息")
//    @RequestMapping(value = "/list/promotion", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<CartPromotionItem>> listPromotion() {
//        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId());
//        return CommonResult.success(cartPromotionItemList);
//    }
    /**
     *  更新商品数量
     *  this.axios.post('/car/update/quantity',Qs.stringify({
     *             id:item.id,
     *             quantity:item.quantity   当前数量
     *           }),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
     */
    @RequestMapping(value = "/update/quantity", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateQuantity(@RequestParam Long id,
                                       @RequestParam Integer quantity) {
        int count = cartItemService.updateQuantity(id, quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


//    @RequestMapping(value = "/getProduct/{productId}", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<CartProduct> getCartProduct(@PathVariable Long productId) {
//        CartProduct cartProduct = cartItemService.getCartProduct(productId);
//        return CommonResult.success(cartProduct);
//    }


//    @RequestMapping(value = "/update/attr", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult updateAttr(@RequestBody OmsCartItem cartItem) {
//        int count = cartItemService.updateAttr(cartItem);
//        if (count > 0) {
//            return CommonResult.success(count);
//        }
//        return CommonResult.failed();
//    }

    /**
     *  删除
     *  this.axios.post('/car/delete',Qs.stringify({
     *             ids:item.id
     *           }),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = cartItemService.delete( ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


//    @RequestMapping(value = "/clear", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult clear() {
//        int count = cartItemService.clear(memberService.getCurrentMember().getId());
//        if (count > 0) {
//            return CommonResult.success(count);
//        }
//        return CommonResult.failed();
//    }
}
