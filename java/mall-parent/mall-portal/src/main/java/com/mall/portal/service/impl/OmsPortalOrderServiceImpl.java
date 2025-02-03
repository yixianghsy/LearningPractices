package com.mall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.api.ResultCode;
import com.mall.exception.ApiException;
import com.mall.exception.BusinessException;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.service.PmsProductService;
import com.mall.mansger.service.PmsSkuStockService;
import com.mall.order.dto.*;
import com.mall.order.model.OmsCartItem;
import com.mall.order.model.OmsOrder;
import com.mall.order.model.OmsOrderItem;
import com.mall.order.model.OrderParam;
import com.mall.order.service.OmsCartItemService;
import com.mall.order.service.OmsOrderService;
import com.mall.portal.service.OmsPortalOrderService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.service.RedisService;
import com.mall.sso.service.UmsMemberReceiveAddressService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 前台订单管理Service
 * Created on 2018/8/30.
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Autowired
    private UmsMemberService memberService;

    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Reference
    PmsSkuStockService pmsSkuStockService;
    @Reference
    private OmsCartItemService cartItemService;
    @Reference
    PmsProductService productService;
    @Reference
    OmsOrderService orderService;
    @Reference
    OmsCartItemService omsCartItemService;
    @Reference
    private UmsMemberReceiveAddressService addressService;
    @Override
    public ConfirmOrderDTO generateConfirmOrder(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            throw new ApiException(ResultCode.VALIDATE_FAILED);
        }
        ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
        // 1.商品
        List<OmsCartItem> omsCartItemsList = cartItemService.listByIds(ids);
        confirmOrderDTO.setCartList(omsCartItemsList);
        // 2、计算价格
        calcCatAmount(confirmOrderDTO);
        // 3.收货地址
        UmsMember currentMember = memberService.getCurrentMember();
//        QueryWrapper<UmsMemberReceiveAddress> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        List<UmsMemberReceiveAddress> list = addressService.list(currentMember.getId());
        confirmOrderDTO.setAddressList(Collections.singletonList(list));

        return confirmOrderDTO;
    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam) throws BusinessException {
        return null;
    }

    @Override
    public OmsOrder generateOrder(OrderParamDTO paramDTO) {
        // 根据购物车id 查询真实库存
        UmsMember currentMember = memberService.getCurrentMember();
        // 根据购物车id查询所有购物车信息
        List<CartItemStockDTO> cartItemStockByIds = omsCartItemService.getCartItemStockByIds(currentMember.getId(),paramDTO.getItemIds());
        // 1. 判断库存（如果没有库存直接提示）
        // 获取库存不足的名称 如果productName为空说明库存未超出
        String productName = hasStock(cartItemStockByIds);

        if(StrUtil.isNotEmpty(productName)){
            throw new ApiException("您的手速过慢，"+productName+"已被别人买走");
        }
        // 如果有库存就进行下单操作：
        //2.保存订单主表oms_order信息  订单号
        OmsOrder omsOrder = newOrder(paramDTO, currentMember, cartItemStockByIds);
        // TODO  delete_status为空无法插入 插入后需返回主键ID
        orderService.orderInsert(omsOrder);
        // 3.保存订单详情表oms_order_item( 购物车转移）
        List<OmsOrderItem> list=newOrderItems(omsOrder,cartItemStockByIds);
        orderService.insertList(list);
        // 4.锁定库存
        lockStock(cartItemStockByIds);
        // 5.删除对应购物车
        removeCartItem(cartItemStockByIds);
        return omsOrder;
    }

    @Override
    public CommonResult getDetailOrder(Long orderId) {
        return null;
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        return null;
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) {

    }

    @Override
    public int deleteOrder(Long orderId) {
        return 0;
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {

    }

    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status) {
        return null;
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {

    }

    @Override
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public OmsOrderDetail detail(Long orderId) {
        return null;
    }

    @Override
    public void paySuccessByOrderSn(String orderSn, Integer payType) {

    }
    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public CommonResult getOrderDetail(Long orderId) {
        return CommonResult.success(orderService.getDetail(orderId));
    }

    /**
     * 计算价格
     * @param confirmOrderDTO
     */
    public void calcCatAmount(ConfirmOrderDTO confirmOrderDTO){
        //计算商品数量
        Integer productTotal=0;
        // 总价
        BigDecimal priceTotal=new BigDecimal(0);
        // 运费
        BigDecimal freightAmount=new BigDecimal(0);

        for (OmsCartItem omsCartItem : confirmOrderDTO.getCartList()) {
            // 商品总件数
            productTotal+=omsCartItem.getQuantity();
            // 总价
            priceTotal= priceTotal.add( omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
            /**
             *SELECT id,brand_id,product_category_id,feight_template_id,product_attribute_category_id,
             * name,pic,product_sn,delete_status,
             * publish_status,new_status,recommand_status,verify_status,sort,sale,price,
             * promotion_price,gift_growth,gift_point,use_point_limit,sub_title,description,
             * original_price,stock,low_stock,unit,weight,preview_status,service_ids,
             * keywords,note,album_pics,detail_title,detail_desc,detail_html,detail_mobile_html,
             * promotion_start_time,promotion_end_time,promotion_per_limit,promotion_type,brand_name,product_category_name
             * FROM pms_product WHERE id=?
             */
            PmsProduct product = productService.getById(omsCartItem.getProductId());
            String serviceIds = product.getServiceIds();
            String[] serviceIdsArray = serviceIds.split(",");
            if(serviceIdsArray.length>0){
                // 判断是否包邮
                if(!ArrayUtil.containsAny(serviceIdsArray, "3")){
                    freightAmount=freightAmount.add(new BigDecimal(10));
                }
            }

        }
        confirmOrderDTO.setProductTotal(productTotal);
        confirmOrderDTO.setPriceTotal(priceTotal);
        confirmOrderDTO.setFreightAmount(freightAmount);
        confirmOrderDTO.setPayAmount(priceTotal.subtract(freightAmount));
    }

    /**
     * 判断是否有库存
     * @param cartItemStockByIds
     * @return
     */
    public String hasStock(List<CartItemStockDTO> cartItemStockByIds){
        for (CartItemStockDTO cart : cartItemStockByIds) {
            // 如果当前购物车商品的规格库存 小于 实际购买数量 就库存不足
            if(cart.getStock()<cart.getQuantity()){
                return cart.getProductName();
            }
        }

        return null;
    }

    // 创建OmsOrder
    public OmsOrder newOrder(OrderParamDTO paramDTO,UmsMember currentMember,List<CartItemStockDTO> cartItemStockByIds){
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setCreateTime(new Date());
        omsOrder.setModifyTime(new Date());
        omsOrder.setMemberId(currentMember.getId());
        omsOrder.setMemberUsername(currentMember.getUsername());

        //  计算价格 需要传入ConfirmOrderDTO
        ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
        // 1.购物车集合 因为计算价格是根据购物车列表信息来计算的
        List<OmsCartItem> omsCartItemsList = new ArrayList<>();
        // 循环将CartItemStockDTO 转换为OmsCartItem
        for (CartItemStockDTO cartItem : cartItemStockByIds) {
            omsCartItemsList.add(cartItem);
        }

        confirmOrderDTO.setCartList(omsCartItemsList);
        // 2、计算价格
        calcCatAmount(confirmOrderDTO);

        // 商品总价
        omsOrder.setTotalAmount(confirmOrderDTO.getPriceTotal());
        // 应付总金额
        omsOrder.setPayAmount(confirmOrderDTO.getPayAmount());
        // 运费金额
        omsOrder.setFreightAmount(confirmOrderDTO.getFreightAmount());
        /*
        促销金额待升级
         */
        // 订单来源：0->PC订单；1->app订单
        omsOrder.setSourceType(1);
        // 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        omsOrder.setStatus(0);
        omsOrder.setOrderType(0);   //订单类型：0->正常订单；1->秒杀订单
        omsOrder.setAutoConfirmDay(15);  // 自动确认收货时间
        // 地址
        UmsMemberReceiveAddress address = addressService.getOne(currentMember.getId(),paramDTO.getMemberReceiveAddressId());
        //收货人姓名
        omsOrder.setReceiverName(address.getName());
        // receiver_phone` varchar(32) NOT NULL COMMENT '收货人电话',
        omsOrder.setReceiverPhone(address.getPhoneNumber());
        //`receiver_post_code` varchar(32) DEFAULT NULL COMMENT '收货人邮编',
        omsOrder.setReceiverPostCode(address.getPostCode());
        //receiver_province` varchar(32) DEFAULT NULL COMMENT '省份/直辖市',
        omsOrder.setReceiverProvince(address.getProvince());
        //城市,
        omsOrder.setReceiverCity(address.getCity());
        // '区'
        omsOrder.setReceiverRegion(address.getRegion());
        //'详细地址'
        omsOrder.setReceiverDetailAddress(address.getDetailAddress());
        // '确认收货状态：0->未确认；1->已确认'
        omsOrder.setConfirmStatus(0);
        // 生产订单编码
        omsOrder.setOrderSn(getOrderSn(omsOrder));
        return omsOrder;
    }

    /**
     * 生成订单编号：生成规则:8位日期+2位平台号码+6位以上自增id；
     *
     * @return
     */
    public String getOrderSn(OmsOrder omsOrder){
        // 订单编号
        StringBuilder sb=new StringBuilder();
        //8位日期
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
        sb.append(yyyyMMdd);
        //2位平台号码  1.pc  2.app
        //String.format：参数说
        // 0 代表前面补充零
        // 6 代表补充长度
        // d 代表正数
        String sourceTyp = String.format("%02d", omsOrder.getSourceType());
        sb.append(sourceTyp);
        // 6位以上自增id
        // redis incr  原子性
        // 适合并发的自增方式：
        String key= REDIS_KEY_PREFIX_ORDER_ID+ yyyyMMdd;
        Long incr = redisService.incr(key, 1);
        // 拿到当前的6位以上自增 如果小于6位，自动补全0
        if(incr.toString().length()<=6) {
            sb.append(String.format("%06d", redisService.incr(key, 1)));
        }
        else {
            // 如果是6位 不用补0
            sb.append(incr);
        }
        return sb.toString();

    }
    /**
     *   `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
     *   `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
     *   `product_id` bigint(20) DEFAULT NULL,
     *   `product_pic` varchar(500) DEFAULT NULL,
     *   `product_name` varchar(200) DEFAULT NULL,
     *   `product_brand` varchar(200) DEFAULT NULL,
     *   `product_sn` varchar(64) DEFAULT NULL,
     *   `product_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
     *   `product_quantity` int(11) DEFAULT NULL COMMENT '购买数量',
     *   `product_sku_id` bigint(20) DEFAULT NULL COMMENT '商品sku编号',
     *   `product_sku_code` varchar(50) DEFAULT NULL COMMENT '商品sku条码',
     *   `product_category_id` bigint(20) DEFAULT NULL COMMENT '商品分类id',
     *   `sp1` varchar(100) DEFAULT NULL COMMENT '商品的销售属性',
     *   `sp2` varchar(100) DEFAULT NULL,
     *   `sp3` varchar(100) DEFAULT NULL,
     *   `promotion_name` varchar(200) DEFAULT NULL COMMENT '商品促销名称',
     *   `promotion_amount` decimal(10,2) DEFAULT NULL COMMENT '商品促销分解金额',
     *   `coupon_amount` decimal(10,2) DEFAULT NULL COMMENT '优惠券优惠分解金额',
     *   `integration_amount` decimal(10,2) DEFAULT NULL COMMENT '积分优惠分解金额',
     *   `real_amount` decimal(10,2) DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
     *   `gift_integration` int(11) DEFAULT '0',
     *   `gift_growth` int(11) DEFAULT '0',
     *   `product_attr` varchar(500) DEFAULT NULL COMMENT '商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]',
     *    生成订单详情
     * @param omsOrder
     * @param cartItemStockByIds
     * @return
     */
    private List<OmsOrderItem> newOrderItems(OmsOrder omsOrder,List<CartItemStockDTO> cartItemStockByIds) {

        List<OmsOrderItem> list=new ArrayList<>();
        for (CartItemStockDTO cartItemStockById : cartItemStockByIds) {
            OmsOrderItem orderItem=new OmsOrderItem();
            orderItem.setOrderId(omsOrder.getId());
            orderItem.setOrderSn(omsOrder.getOrderSn());
            orderItem.setProductId(cartItemStockById.getProductId());
            orderItem.setProductPic(cartItemStockById.getProductPic());
            orderItem.setProductName(cartItemStockById.getProductName());
            orderItem.setProductBrand(cartItemStockById.getProductBrand());
            orderItem.setProductSn(cartItemStockById.getProductSn());
            orderItem.setProductPrice(cartItemStockById.getPrice());
            orderItem.setProductQuantity(cartItemStockById.getQuantity());
            orderItem.setProductSkuId(cartItemStockById.getProductSkuId());
            orderItem.setProductSkuCode(cartItemStockById.getProductSkuCode());
            orderItem.setProductCategoryId(cartItemStockById.getProductCategoryId());
            orderItem.setSp1(cartItemStockById.getSp1());
            orderItem.setSp2(cartItemStockById.getSp2());
            orderItem.setSp3(cartItemStockById.getSp3());
            list.add(orderItem);
        }
        return list;
    }

    /**
     * 锁定库存  把当前的购买数累加到sku lock_stock中
     * @param cartItemStockByIds
     */
    private void lockStock(List<CartItemStockDTO> cartItemStockByIds) {
        for (CartItemStockDTO cart : cartItemStockByIds) {
//            UpdateWrapper<PmsSkuStock> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.setSql("lock_stock=lock_stock+"+cart.getQuantity())
//                    .lambda()
//                    .eq(PmsSkuStock::getId,cart.getProductSkuId());
            pmsSkuStockService.batchUpdate(cart.getQuantity(),cart.getProductSkuId());
//            skuStockService.update(updateWrapper);
        }
    }
    /**
     * 删除订单后的购物车
     * @param cartItemStockByIds
     */
    private void removeCartItem(List<CartItemStockDTO> cartItemStockByIds) {
        // 1.购物车集合
        List<Long> ids = new ArrayList<>();
        for (CartItemStockDTO cartItem : cartItemStockByIds) {
            ids.add(cartItem.getId());
        }
        // 移除购物车信息
        cartItemService.removeByIds(ids);
//        cartItemService.removeByIds(ids);
    }
}
