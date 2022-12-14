package com.tulingxueyuan.mall.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.common.exception.ApiException;
import com.tulingxueyuan.mall.dto.OrderDetailDTO;
import com.tulingxueyuan.mall.dto.OrderListDTO;
import com.tulingxueyuan.mall.dto.OrderParamDTO;
import com.tulingxueyuan.mall.modules.oms.model.OmsOrder;
import com.tulingxueyuan.mall.modules.oms.service.OmsOrderService;
import com.tulingxueyuan.mall.modules.oms.service.TradeService;
import com.tulingxueyuan.mall.dto.ConfirmOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RestController
@Api(tags = "OrderController",description = "订单服务接口")
@RequestMapping("/order")
@Slf4j
public class OrderController {


    @Autowired
    OmsOrderService orderService;
    @Autowired
    TradeService tradeService;

    /**
     *  加入购物车---生成确认订单实现
     *  立即购买—生成确认订单实现 product_id  sku_id. 改成DTO接收
     *    复用业务逻辑的代码 product_id 和sku_id 查出购物车对象所需要信息
     *  初始化确认订单的商品和收货地址信息
     * this.axios.post('/order/generateConfirmOrder',Qs.stringify({itemIds: constStore.itemids}
     */
    @RequestMapping(value="generateConfirmOrder",method = RequestMethod.POST)
    public CommonResult generateConfirmOrder(
            @RequestParam("itemIds") List<Long> ids
    ){
        ConfirmOrderDTO confirmOrderDTO= orderService.generateConfirmOrder(ids);
        return CommonResult.success(confirmOrderDTO) ;
    }

    /**
     *  生成订单(下单）
     * this.axios
     *           .post("/order/generateOrder", {
     */
    @RequestMapping(value="/generateOrder",method = RequestMethod.POST)
    public CommonResult generateOrder(@RequestBody OrderParamDTO paramDTO){
        OmsOrder omsOrder = orderService.generateOrder(paramDTO);
        return CommonResult.success(omsOrder.getId());
    }

    /**
     *  读取下单成功后的订单详情
     * this.axios.get(`/order/orderDetail?orderId=${this.orderId}`).then((res)=>{
     */
    @RequestMapping(value="/orderDetail")
    public CommonResult getOrderDetail(@RequestParam("orderId")Long id){
        OrderDetailDTO dto=orderService.getOrderDetail(id);
        return  CommonResult.success(dto);
    }

    /**
     *  我的订单列表
     * this.axios.post('/order/list/userOrder',Qs.stringify({
     pageSize:10,
     pageNum:this.pageNum
     */
    @RequestMapping(value="/list/userOrder",method = RequestMethod.POST)
    public CommonResult getMyOrders(
            @RequestParam(value="pageSize",defaultValue = "10")Integer pageSize,
            @RequestParam(value="pageNum",defaultValue = "1")Integer pageNum) {
        IPage<OrderListDTO> myOrders = orderService.getMyOrders(pageSize, pageNum);

        return CommonResult.success(myOrders);
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
    public  CommonResult tradeQrCode(@RequestParam("orderId") Long orderId,
                                     @RequestParam("payType") Integer payType){
        if(payType>2 || payType<0){
            throw  new ApiException("支付类型参数错误！");
        }
        return tradeService.tradeQrCode(orderId,payType);
    }

    @RequestMapping(value="/paySuccess/{payType}",method = RequestMethod.POST)
    public void getMyOrders(
            @PathVariable("payType") Integer payType,
            HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
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

        String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwUpyecFNQ+yaW2jWzQA71LEcT1xk62vOpNukZFOWCDTKxL2WY6OVv4uh2RajKFdslHfWjvwooXr5TsKtvnv0iRLDjbsuh6H6va5jWa4w2FPOqSGybQzDBSAAx2bAWsjdP/wtHxISyI0JTsxCstjHYippkbvZsa4cDCn3a/7NQuIwrxwlzI3F5AVitENJcRBWDZlbbu1HKp3Af9MDRCj91nNlZrnJZ27CWzPiNhoW5wvo9a1I2+qUQoBvyKYxY0mPeBnSngaYTNXN8RU1MiTwQ6sri2TblSV8EXR+4org82NgjWdWM4L7Bsnz/bA2OTP7jMC1EePnAcsdvUwQPnbQtwIDAQAB";

        // 验签  ：去除sign和sign_type 参数 进行验签， checkV1 会在方法中去除，CheckV2不会去除sign_type，所以要手动排除
        boolean signVerified = AlipaySignature.rsaCheckV2(params, alipay_public_key, "utf-8","RSA2"); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            // 订单id
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            if(StrUtil.isNotBlank(out_trade_no) && NumberUtil.isNumber(out_trade_no)){
                Long orderId=Long.parseLong(out_trade_no);
                orderService.paySuccess(orderId,payType);
                log.info("支付成功");
            }
        }else {
            System.out.println("验签失败");
        }


    }
}
