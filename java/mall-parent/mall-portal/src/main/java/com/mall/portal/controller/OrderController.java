package com.mall.portal.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.mall.api.CommonResult;
import com.mall.exception.ApiException;
import com.mall.order.dto.ConfirmOrderDTO;
import com.mall.order.dto.OmsOrderDetail;
import com.mall.order.dto.OrderDetailDTO;
import com.mall.order.dto.OrderParamDTO;
import com.mall.order.model.OmsOrder;
import com.mall.order.service.OmsOrderService;
import com.mall.portal.service.OmsPortalOrderService;
import com.mall.portal.service.TradeService;
import com.mall.portal.service.UmsMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
// TODO 购物车
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OmsPortalOrderService omsPortalOrderService;

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private TradeService tradeService;
    @Autowired
    private UmsMemberService umsMemberService;

    @Reference
    private OmsOrderService orderService;
    /**
     *  加入购物车---生成确认订单实现
     *  立即购买—生成确认订单实现 product_id  sku_id. 改成DTO接收
     *    复用业务逻辑的代码 product_id 和sku_id 查出购物车对象所需要信息
     *  初始化确认订单的商品和收货地址信息
     * this.axios.post('/order/generateConfirmOrder',Qs.stringify({itemIds: constStore.itemids}
     */
    @RequestMapping(value="generateConfirmOrder",method = RequestMethod.POST)
    public CommonResult generateConfirmOrder(
            @RequestParam("itemIds") List<Long> itemIds
    ){
        ConfirmOrderDTO confirmOrderDTO= omsPortalOrderService.generateConfirmOrder(itemIds);
        return CommonResult.success(confirmOrderDTO) ;
    }

    /**
     *  生成订单(下单）
     * this.axios
     *           .post("/order/generateOrder", {
     */
    @RequestMapping(value="/generateOrder",method = RequestMethod.POST)
    public CommonResult generateOrder(@RequestBody OrderParamDTO paramDTO){
        OmsOrder omsOrder = portalOrderService.generateOrder(paramDTO);
        return CommonResult.success(omsOrder.getId());
    }

    /**
     *  读取下单成功后的订单详情
     * this.axios.get(`/order/orderDetail?orderId=${this.orderId}`).then((res)=>{
     */
    @RequestMapping(value="/orderDetail")
    public CommonResult getOrderDetail(@RequestParam("orderId")Long id){
        return portalOrderService.getOrderDetail(id);
    }

    /**
     *  我的订单列表
     * this.axios.post('/order/list/userOrder',Qs.stringify({
     pageSize:10,
     pageNum:this.pageNum
     */
    @RequestMapping(value="/list/userOrder",method = RequestMethod.POST)
    public CommonResult<List<OmsOrderDetail>> getMyOrders(
            @RequestParam(value="pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value="pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "status",required = false) Integer status) {
        Long memberId = umsMemberService.getCurrentMember().getId();
        if(memberId == null || (status!=null && status > 4)){
            return CommonResult.validateFailed();
        }
        return orderService.findMemberOrderList(pageSize,pageNum,memberId,status);
    }


    /**
     * 生成当面付二维码
     *  /order/tradeQrCode
     orderId:this.orderId,
     payType:1
     */

    @RequestMapping(value="tradeQrCode",method = RequestMethod.POST)
    @ResponseBody
    public  CommonResult tradeQrCode(@RequestParam("orderId") Long orderId,
                                     @RequestParam("payType") Integer payType){
        if(payType>2 || payType<0){
            throw  new ApiException("支付类型参数错误！");
        }
        return tradeService.tradeQrCode(orderId,payType);
    }

    @RequestMapping(value = "/tradeStatusQuery",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult tradeStatusQuery(@RequestParam(value = "orderId") Long orderId,
                                         @RequestParam(value = "payType") Integer payType){

        if(payType > 2 || payType < 0){
            throw new IllegalArgumentException("支付类型不正确，平台目前仅支持支付宝与微信支付");
        }
        return tradeService.tradeStatusQuery(orderId,payType);
    }
}
