package com.mall.portal.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.api.Result;
import com.mall.exception.ApiException;
import com.mall.exception.BusinessException;
import com.mall.order.dto.OrderParamDTO;
import com.mall.order.model.OmsOrder;
import com.mall.order.service.OmsOrderService;
import com.mall.portal.domain.OmsOrderDetail;
import com.mall.portal.config.Configs;
import com.mall.portal.domain.OrderParam;
import com.mall.portal.dto.ConfirmOrderDTO;
import com.mall.portal.service.OmsPortalOrderService;
import com.mall.portal.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import  com.mall.portal.service.UmsMemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 订单管理Controller
 * Created on 2018/8/30.
 */
@Slf4j
@Controller
@Api(tags = "OmsPortalOrderController",description = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {
    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UmsMemberService umsMemberService;

    @Reference
    OmsOrderService orderService;

    @Autowired
    private WebSocketController webSocketController;
//@Autowired
//OmsOrderService orderService;

    @Autowired
    OmsPortalOrderService omsPortalOrderService;
    /**
     * 加入购物车---生成确认订单实现
     * 立即购买—生成确认订单实现 product_id  sku_id. 改成DTO接收
     * 复用业务逻辑的代码 product_id 和sku_id 查出购物车对象所需要信息
     * 初始化确认订单的商品和收货地址信息
     * this.axios.post('/order/generateConfirmOrder',Qs.stringify({itemIds: constStore.itemids}
     */
    @ApiOperation("根据购物车信息生成确认单信息")
    @ApiImplicitParam(name = "itemId",value = "购物车选择购买的选项ID",allowMultiple = true,paramType = "query",dataType = "long")
    @RequestMapping(value = "/generateConfirmOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ConfirmOrderDTO> generateConfirmOrder(@RequestParam(value = "itemIds") List<Long> itemIds) throws BusinessException {
//        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(itemIds);
        ConfirmOrderDTO confirmOrderDTO= omsPortalOrderService.generateConfirmOrder(itemIds);
        return CommonResult.success(confirmOrderDTO) ;
    }

//    @ApiOperation("根据购物车信息生成订单")
//    @RequestMapping(value = "/generateOrder",method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult generateOrder(@RequestBody OrderParam orderParam) throws BusinessException {
//        CommonResult commonResult = portalOrderService.generateOrder(orderParam);
//        System.out.println(commonResult);
//        return commonResult;
//    }
    /**
     *  生成订单(下单）
     * this.axios
     *           .post("/order/generateOrder", {
     */
    @RequestMapping(value="/generateOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateOrder(@RequestBody OrderParamDTO paramDTO){
        OmsOrder omsOrder = portalOrderService.generateOrder(paramDTO);
        System.out.println("omsOrder.getId()"+omsOrder.getId());
        return CommonResult.success(omsOrder.getId());
    }
//    @ApiOperation("查看订单详情#杨过添加")
//    @RequestMapping(value = "/orderDetail",method = {RequestMethod.POST,RequestMethod.GET})
//    @ResponseBody
//    public CommonResult orderDetail(@RequestParam Long orderId){
//        return portalOrderService.getDetailOrder(orderId);
//    }
    /**
     *  读取下单成功后的订单详情
     * this.axios.get(`/order/orderDetail?orderId=${this.orderId}`).then((res)=>{
     */
    @RequestMapping(value="/orderDetail")
    @ResponseBody
    public CommonResult getOrderDetail(@RequestParam("orderId")Long id){
        return  CommonResult.success(portalOrderService.getDetailOrder(id));
    }

//    @ApiOperation("支付成功的回调")
//    @ApiImplicitParams({@ApiImplicitParam(name = "payType", value = "支付方式:0->未支付,1->支付宝支付,2->微信支付",
//            allowableValues = "1,2", paramType = "query", dataType = "integer")})
//    @RequestMapping(value = "/paySuccess/{payType}",method = RequestMethod.POST)
//    @ResponseBody
//    public void paySuccess(@PathVariable Integer payType,
//                             HttpServletRequest request,
//                             HttpServletResponse response) throws AlipayApiException {
//        if(payType > 2 || payType < 0){
//            throw new IllegalArgumentException("支付类型不正确，平台目前仅支持支付宝与微信支付");
//        }
//        if(payType == 1){//支付宝支付
//            //1、获取request里所有与alipay相关的参数，封装成一个map
//            Map<String,String> param = Maps.newHashMap();
//            Enumeration<String> parameterNames = request.getParameterNames();
//            while (parameterNames.hasMoreElements()){
//                String parameterName = parameterNames.nextElement();
//                log.info("alipay callback parameters:-->"
//                        +parameterName+":->" +request.getParameter(parameterName));
//                if(!parameterName.toLowerCase().equals("sign_type")){
//                    param.put(parameterName,request.getParameter(parameterName));
//                }
//            }
//            // 2、验证请求是否是alipay返回的请求内容【验证请求合法性】
//            // 很重要
//            boolean isPassed = AlipaySignature.rsaCheckV2(param, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
//            PrintWriter out = null;
//            try {
//                out = response.getWriter();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(isPassed){
//                Long orderId = Long.parseLong(param.get("out_trade_no"));
//                int count = portalOrderService.paySuccess(orderId,payType);
//                if(count > 0){
//                    log.info("支付成功，订单完成支付");
//                    out.print("success");
//                }else{
//                    log.info("支付失败，订单未能完成支付");
//                    out.print("unSuccess");
//                }
//            }else{
//                log.info("支付失败，订单未能完成支付");
//                out.print("unSuccess");
//            }
//        }else if(payType == 2){//微信支付
//
//        }
//    }


        @RequestMapping(value="/paySuccess/{payType}",method = RequestMethod.POST)
        @ResponseBody
        public void getMyOrders(
                @PathVariable("payType") Integer payType,
                HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException, JsonProcessingException {
            //获取支付宝GET过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                if(!name.toLowerCase().equals("sign_type")) {
                    String[] values = (String[]) requestParams.get(name);
                    String valueStr = "";
                    for (int i = 0; i < values.length; i++) {
                        valueStr = (i == values.length - 1) ? valueStr + values[i]
                                : valueStr + values[i] + ",";
                    }
                    //乱码解决，这段代码在出现乱码时使用
                    //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                    params.put(name, valueStr);
                }
            }

            String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyQVNvsgGz1JWMX+8cQw/+xtA56EbxghWBeArkOM59Y7bokDKR/DTYn6mK46vEhA8IXvt86yaQkBJQ+a4SM0DJgqBe64qKBdEocXfhDG69eEFrbV3Eu7OMWNx7SWXWvMr9Pwvw2jQItDW00ADLpRuLFRmEivZZ2duDVmjwogxsrF53OIeghfW9KI+LCxun7RbKxVFyBzPeOKzQ0jddO7wkjnBh/1bGaajTvzO9YnXEN/PnOepcazYKdxKh3cK93XSsAp75cKI+t5b1H7Im0fKst3PdcnSBFCZ9IzvMlOoM5Pc+y7bduOovOcSD3P8l62SYDqBhdYLhUBGRcEhPhBbaQIDAQAB";

            // 验签  ：去除sign和sign_type 参数 进行验签， checkV1 会在方法中去除，CheckV2不会去除sign_type，所以要手动排除
            boolean signVerified = AlipaySignature.rsaCheckV2(params, alipay_public_key, "utf-8","RSA2"); //调用SDK验证签名

            //——请在这里编写您的程序（以下代码仅作参考）——
            if(signVerified) {
                // 订单id
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
                if(StrUtil.isNotBlank(out_trade_no) && NumberUtil.isNumber(out_trade_no)){
                    Long orderId=Long.parseLong(out_trade_no);
                    //TODO 代码需实现代码需补全
                    orderService.paySuccess(orderId,payType);
                    ObjectMapper objectMapper = new ObjectMapper();
                    // 调用 WebSocket 通知页面扫码结果
                    webSocketController.sendMessage(objectMapper.writeValueAsString(new Result<>(200, "Pay success.")));
                    log.info("支付成功");
                }
            }else {
                System.out.println("验签失败");
            }


        }

    @ApiOperation("自动取消超时订单")
    @RequestMapping(value = "/cancelTimeOutOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelTimeOutOrder(){
        return portalOrderService.cancelTimeOutOrder();
    }

    @ApiOperation("取消单个超时订单")
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelOrder(Long orderId){
        portalOrderService.sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null);
    }
    @ApiOperation("按状态分页获取用户订单列表")
    @ApiImplicitParam(name = "status", value = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            defaultValue = "-1", allowableValues = "-1,0,1,2,3,4", paramType = "query", dataType = "int")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OmsOrderDetail>> list(@RequestParam Integer status,
                                                         @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        //  TODO sql 语句有问题需修复
        CommonPage<OmsOrderDetail> orderPage = portalOrderService.list(status,pageNum,pageSize);
        return CommonResult.success(orderPage);
    }
    @ApiOperation("根据ID获取订单详情")
    @RequestMapping(value = "/detail/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrderDetail> detail(@PathVariable Long orderId) {
        OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
        return CommonResult.success(orderDetail);
    }
    @ApiOperation("用户取消订单")
    @RequestMapping(value = "/cancelUserOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelUserOrder(Long orderId) {
        portalOrderService.cancelOrder(orderId);
        return CommonResult.success(null);
    }

    @ApiOperation("用户确认收货")
    @RequestMapping(value = "/confirmReceiveOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult confirmReceiveOrder(Long orderId) {
        portalOrderService.confirmReceiveOrder(orderId);
        return CommonResult.success(null);
    }


    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     */
    @ApiOperation(value = "删除会员订单#杨过添加",notes = "status为：3->已完成；4->已关闭；5->无效订单，才可以删除，否则只能先取消订单然后删除")
    @RequestMapping(value = "/deleteOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteOrder(Long orderId){
        int total = portalOrderService.deleteOrder(orderId);
        if(total > 0){
            return CommonResult.success("有："+total+"：条订单被删除");
        }else{
            return CommonResult.failed("订单已经被删除或者没有符合条件的订单");
        }
    }

    /**
     * 订单服务由会员服务调用，会员服务传来会员：ID
     * @param status
     *      null查询所有
     *      订单状态0->待付款；1->待发货；2->已发货；3->已完成;4->已关闭；
     * @return
     */
    @ApiOperation("会员订单查询#杨过添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "订单状态:0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
                    allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")})
    @RequestMapping(value = "/list/userOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "status",required = false) Integer status){
        Long memberId = umsMemberService.getCurrentMember().getId();
        if(memberId == null || (status!=null && status > 4)){
            return CommonResult.validateFailed();
        }
        return portalOrderService.findMemberOrderList(pageSize,pageNum,memberId,status);
    }
    /**
     * 生成当面付二维码
     *  /order/tradeQrCode
     orderId:this.orderId,
     payType:1
     */
    @ApiOperation("支付接口，只实现支付宝支付，微信支付暂未实现")
    @ApiImplicitParams({@ApiImplicitParam(name="orderId",value = "订单id"),
            @ApiImplicitParam(name="payType",value = "支付类型1:支付宝2：微信"
                    ,allowableValues = "1,2",dataType = "integer")})
    @RequestMapping(value="tradeQrCode",method = RequestMethod.POST)
    @ResponseBody
    public  CommonResult tradeQrCode(@RequestParam("orderId") Long orderId,
                                     @RequestParam("payType") Integer payType){
        if(payType>2 || payType<0){
            throw  new ApiException("支付类型参数错误！");
        }
        return tradeService.tradeQrCode(orderId,payType);
    }

    @ApiOperation("订单支付状态查询,手动查询#实现支付宝查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "payType", value = "支付方式:0->未支付,1->支付宝支付,2->微信支付",
            allowableValues = "1,2", paramType = "query", dataType = "integer")})
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
