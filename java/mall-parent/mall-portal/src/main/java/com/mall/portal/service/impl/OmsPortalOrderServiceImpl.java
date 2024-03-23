package com.mall.portal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.api.ResultCode;
import com.mall.exception.ApiException;
import com.mall.exception.Asserts;
import com.mall.exception.BusinessException;
import com.mall.mansger.mapper.PmsSkuStockMapper;
import com.mall.mansger.model.SmsCoupon;
import com.mall.marketing.mapper.SmsCouponHistoryMapper;
//import com.mall.mansger.model.*;
import com.mall.marketing.model.SmsCouponHistory;
import com.mall.marketing.model.SmsCouponHistoryExample;
import com.mall.marketing.model.SmsCouponProductCategoryRelation;
import com.mall.marketing.model.SmsCouponProductRelation;
import com.mall.order.dto.CartItemStockDTO;
import com.mall.order.dto.OrderParamDTO;
import com.mall.order.mapper.*;
import com.mall.portal.domain.OmsOrderDetail;
import com.mall.order.model.*;
import com.mall.portal.dao.PortalOrderDao;
import com.mall.portal.dao.PortalOrderItemDao;
import com.mall.portal.dao.SmsCouponHistoryDao;
import com.mall.portal.domain.CartPromotionItem;
import com.mall.portal.domain.ConfirmOrderResult;
import com.mall.portal.domain.OrderParam;
import com.mall.portal.domain.SmsCouponHistoryDetail;
import com.mall.portal.dto.ConfirmOrderDTO;
import com.mall.portal.service.*;
import com.mall.sso.mapper.UmsIntegrationConsumeSettingMapper;
import com.mall.sso.mapper.UmsMemberReceiveAddressMapper;
import com.mall.sso.model.UmsIntegrationConsumeSetting;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.model.UmsMemberReceiveAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前台订单管理Service
 * Created on 2018/8/30.
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;
    @Autowired
    private UmsMemberCouponService memberCouponService;
    @Autowired
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private SmsCouponHistoryDao couponHistoryDao;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private PortalOrderItemDao orderItemDao;
    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Autowired
    private PortalOrderDao portalOrderDao;
    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private  UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Autowired
    PmsProductService productService;
    @Autowired
    private UmsMemberReceiveAddressService addressService;

    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private  PmsSkuStockMapper pmsSkuStockMapper;

    //TODO  涉及支付宝调用
//    @Autowired
//    private CancelOrderSender cancelOrderSender;

    /**
     * 查询会员订单
     * @param memberId
     *          会员ID
     * @param status
     *          订单状态
     * @return
     *      CommonResult<List>
     */
    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize,Integer pageNum,Long memberId,Integer status) {
        PageHelper.startPage(pageNum,pageSize);
        return CommonResult.success(portalOrderDao.findMemberOrderList(memberId,status));
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {
        UmsMember member = memberService.getCurrentMember();
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        if(!member.getId().equals(order.getMemberId())){
            Asserts.fail("不能确认他人订单！");
        }
        if(order.getStatus()!=2){
            Asserts.fail("该订单还未发货！");
        }
        order.setStatus(3);
        order.setConfirmStatus(1);
        order.setReceiveTime(new Date());
        orderMapper.updateByPrimaryKey(order);
    }

    @Override
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        if(status==-1){
            status = null;
        }
        UmsMember member = memberService.getCurrentMember();
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderExample orderExample = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0)
                .andMemberIdEqualTo(member.getId());
        if(status!=null){
            criteria.andStatusEqualTo(status);
        }
        orderExample.setOrderByClause("create_time desc");
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        CommonPage<OmsOrder> orderPage = CommonPage.restPage(orderList);
        //设置分页信息
        CommonPage<OmsOrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(orderPage.getPageNum());
        resultPage.setPageSize(orderPage.getPageSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setTotalPage(orderPage.getTotalPage());
        if(CollUtil.isEmpty(orderList)){
            return resultPage;
        }
        //设置数据信息
        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).collect(Collectors.toList());
        OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
        orderItemExample.createCriteria().andOrderIdIn(orderIds);
        List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        List<OmsOrderDetail> orderDetailList = new ArrayList<>();
        for (OmsOrder omsOrder : orderList) {
            OmsOrderDetail orderDetail = new OmsOrderDetail();
            BeanUtil.copyProperties(omsOrder,orderDetail);
            List<OmsOrderItem> relatedItemList = orderItemList.stream().filter(item -> item.getOrderId().equals(orderDetail.getId())).collect(Collectors.toList());
            orderDetail.setOrderItemList(relatedItemList);
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }

    @Override
    public OmsOrderDetail detail(Long orderId) {
        OmsOrder omsOrder = orderMapper.selectByPrimaryKey(orderId);
        OmsOrderItemExample example = new OmsOrderItemExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(example);
        OmsOrderDetail orderDetail = new OmsOrderDetail();
        BeanUtil.copyProperties(omsOrder,orderDetail);
        orderDetail.setOrderItemList(orderItemList);
        return orderDetail;
    }

    @Override
    public void paySuccessByOrderSn(String orderSn, Integer payType) {
        OmsOrderExample example =  new OmsOrderExample();
        example.createCriteria()
                .andOrderSnEqualTo(orderSn)
                .andStatusEqualTo(0)
                .andDeleteStatusEqualTo(0);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(orderList)){
            OmsOrder order = orderList.get(0);
            paySuccess(order.getId(),payType);
        }
    }

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     *      受影响的行数
     */
    @Override
    public int deleteOrder(Long orderId){
        return portalOrderDao.deleteOrder(orderId);
    }

//    /**
//     * 确认选择购买的商品
//     * @param itemIds
//     *        选择的购物车商品
//     * @return
//     */
//    @Override
//    public ConfirmOrderResult generateConfirmOrder(List<Long> itemIds) throws BusinessException {
//        ConfirmOrderResult result = new ConfirmOrderResult();
//        //获取购物车信息
//        UmsMember currentMember = memberService.getCurrentMember();
//        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(currentMember.getId(),itemIds);
//        result.setCartPromotionItemList(cartPromotionItemList);
//        //获取用户收货地址列表
//        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
//        result.setMemberReceiveAddressList(memberReceiveAddressList);
//        //获取用户可用优惠券列表
//        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
//        result.setCouponHistoryDetailList(couponHistoryDetailList);
//        //获取用户积分
//        result.setMemberIntegration(currentMember.getIntegration());
//        //获取积分使用规则
//        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
//        result.setIntegrationConsumeSetting(integrationConsumeSetting);
//        //计算总金额、活动优惠、应付金额
//        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
//        result.setCalcAmount(calcAmount);
//        return result;
//    }
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
        confirmOrderDTO.setAddressList(list);

        return confirmOrderDTO;
    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam) throws BusinessException {
        return null;
    }

    /**
     * 生成订单
     * @param orderParam
     * @return
     */
//    @Override
//    public CommonResult generateOrder(OrderParam orderParam) throws BusinessException {
//        List<OmsOrderItem> orderItemList = new ArrayList<>();
//        UmsMember currentMember = memberService.getCurrentMember();
//        /*
//         * 杨过修改业务,原业务为清空购物车，改为选择购物车商品下单
//         */
//        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(currentMember.getId(),orderParam.getItemIds());
//        //cartItemService.listPromotion(currentMember.getId());
//        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
//            //生成下单商品信息
//            OmsOrderItem orderItem = new OmsOrderItem();
//            orderItem.setProductId(cartPromotionItem.getProductId());
//            orderItem.setProductName(cartPromotionItem.getProductName());
//            orderItem.setProductPic(cartPromotionItem.getProductPic());
//            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
//            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
//            orderItem.setProductSn(cartPromotionItem.getProductSn());
//            orderItem.setProductPrice(cartPromotionItem.getPrice());
//            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
//            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
//            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
//            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
//            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
//            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
//            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
//            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
//            orderItemList.add(orderItem);
//        }
//        //判断购物车中商品是否都有库存
//        if (!hasStock(cartPromotionItemList)) {
//            return CommonResult.failed("库存不足，无法下单");
//        }
//        //判断是否使用了优惠券
//        if (orderParam.getCouponId() == null) {
//            //不用优惠券
//            for (OmsOrderItem orderItem : orderItemList) {
//                orderItem.setCouponAmount(new BigDecimal(0));
//            }
//        } else {
//            //使用优惠券
//            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
//            if (couponHistoryDetail == null) {
//                return CommonResult.failed("该优惠券不可用");
//            }
//            //对下单商品的优惠券进行处理
//            handleCouponAmount(orderItemList, couponHistoryDetail);
//        }
//        //判断是否使用积分
//        if (orderParam.getUseIntegration() == null) {
//            //不使用积分
//            for (OmsOrderItem orderItem : orderItemList) {
//                orderItem.setIntegrationAmount(new BigDecimal(0));
//            }
//        } else {
//            //使用积分
//            BigDecimal totalAmount = calcTotalAmount(orderItemList);
//            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember, orderParam.getCouponId() != null);
//            if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
//                return CommonResult.failed("积分不可用");
//            } else {
//                //可用情况下分摊到可用商品中
//                for (OmsOrderItem orderItem : orderItemList) {
//                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
//                    orderItem.setIntegrationAmount(perAmount);
//                }
//            }
//        }
//        //计算order_item的实付金额
//        handleRealAmount(orderItemList);
//        //进行库存锁定
//        lockStock(cartPromotionItemList);
//        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
//        OmsOrder order = new OmsOrder();
//        order.setDiscountAmount(new BigDecimal(0));
//        order.setTotalAmount(calcTotalAmount(orderItemList));
//        order.setFreightAmount(new BigDecimal(0));
//        order.setPromotionAmount(calcPromotionAmount(orderItemList));
//        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
//        if (orderParam.getCouponId() == null) {
//            order.setCouponAmount(new BigDecimal(0));
//        } else {
//            order.setCouponId(orderParam.getCouponId());
//            order.setCouponAmount(calcCouponAmount(orderItemList));
//        }
//        if (orderParam.getUseIntegration() == null) {
//            order.setIntegration(0);
//            order.setIntegrationAmount(new BigDecimal(0));
//        } else {
//            order.setIntegration(orderParam.getUseIntegration());
//            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
//        }
//        order.setPayAmount(calcPayAmount(order));
//        //转化为订单信息并插入数据库
//        order.setMemberId(currentMember.getId());
//        order.setCreateTime(new Date());
//        order.setMemberUsername(currentMember.getUsername());
//        //支付方式：0->未支付；1->支付宝；2->微信
//        order.setPayType(orderParam.getPayType());
//        //订单来源：0->PC订单；1->app订单
//        order.setSourceType(1);
//        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
//        order.setStatus(0);
//        //订单类型：0->正常订单；1->秒杀订单
//        order.setOrderType(0);
//        //收货人信息：姓名、电话、邮编、地址
//        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
//        order.setReceiverName(address.getName());
//        order.setReceiverPhone(address.getPhoneNumber());
//        order.setReceiverPostCode(address.getPostCode());
//        order.setReceiverProvince(address.getProvince());
//        order.setReceiverCity(address.getCity());
//        order.setReceiverRegion(address.getRegion());
//        order.setReceiverDetailAddress(address.getDetailAddress());
//        //0->未确认；1->已确认
//        order.setConfirmStatus(0);
//        order.setDeleteStatus(0);
//        //计算赠送积分
//        order.setIntegration(calcGifIntegration(orderItemList));
//        //计算赠送成长值
//        order.setGrowth(calcGiftGrowth(orderItemList));
//        //生成订单号
//        order.setOrderSn(generateOrderSn(order));
//        // TODO: 2018/9/3 bill_*,delivery_*
//        //插入order表和order_item表
//        orderMapper.insert(order);
//        for (OmsOrderItem orderItem : orderItemList) {
//            orderItem.setOrderId(order.getId());
//            orderItem.setOrderSn(order.getOrderSn());
//        }
//        // TODO 请求错误:->nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'sp1' in 'class com.mall.order.model.OmsOrderItem'
//        orderItemDao.insertList(orderItemList);
//        //如使用优惠券更新优惠券使用状态
//        if (orderParam.getCouponId() != null) {
//            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
//        }
//        //如使用积分需要扣除积分
//        if (orderParam.getUseIntegration() != null) {
//            order.setUseIntegration(orderParam.getUseIntegration());
//            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
//        }
//        //删除购物车中的下单商品
//        deleteCartItemList(cartPromotionItemList, currentMember);
//        Map<String, Object> result = new HashMap<>();
//        result.put("order", order);
//        result.put("orderItemList", orderItemList);
//        return CommonResult.success(result, "下单成功");
//    }

    @Override
    public OmsOrder generateOrder(OrderParamDTO paramDTO) {
        // 根据购物车id 查询真实库存
        UmsMember currentMember = memberService.getCurrentMember();
        // 根据购物车id查询所有购物车信息
        List<CartItemStockDTO> cartItemStockByIds = cartItemMapper.getCartItemStockByIds(currentMember.getId(),paramDTO.getItemIds());
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
        orderMapper.insert(omsOrder);
        // 3.保存订单详情表oms_order_item( 购物车转移）
        List<OmsOrderItem> list=newOrderItems(omsOrder,cartItemStockByIds);
        orderItemDao.insertList(list);
        // 4.锁定库存
        lockStock(cartItemStockByIds);
        // 5.删除对应购物车
        removeCartItem(cartItemStockByIds);
        return omsOrder;
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    public CommonResult getDetailOrder(Long orderId){
        return CommonResult.success(portalOrderDao.getDetail(orderId));
    }

    @Override
    public Integer paySuccess(Long orderId,Integer payType) {
        //修改订单支付状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPayType(payType);
        order.setPaymentTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        //恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
        return portalOrderDao.updateSkuStock(orderDetail.getOrderItemList());
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        //查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeOutOrders = portalOrderDao.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeOutOrders)) {
            return CommonResult.failed("暂无超时订单");
        }
        //修改订单状态为交易取消
        List<Long> ids = new ArrayList<>();
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            ids.add(timeOutOrder.getId());
        }
        portalOrderDao.updateOrderStatus(ids, 4);
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            if(CollectionUtils.isEmpty(timeOutOrder.getOrderItemList())){
                continue;
                //throw new RuntimeException("订单产品不允许为空");
            }
            //解除订单商品库存锁定
            portalOrderDao.releaseSkuStockLock(timeOutOrder.getOrderItemList());
            //修改优惠券使用状态
            updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
            //返还使用积分
            if (timeOutOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(timeOutOrder.getMemberId());
                memberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
            }
        }
        return CommonResult.success(null);
    }

    @Override
    public void cancelOrder(Long orderId) {
        //查询为付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return;
        }
        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            //修改订单状态为取消
            cancelOrder.setStatus(4);
            orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            //解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                portalOrderDao.releaseSkuStockLock(orderItemList);
            }
            //修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            //返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(cancelOrder.getMemberId());
                memberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
            }
        }
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        //获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        //发送延迟消息
        //TODO  mq未搭建后续补充
//        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_KEY_PREFIX_ORDER_ID + date;
        Long increment = redisService.increment(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }

    /**
     * 计算该订单赠送的成长值
     */
    private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
        Integer sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum = sum + orderItem.getGiftGrowth() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 计算该订单赠送的积分
     */
    private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 将优惠券信息更改为指定状态
     *
     * @param couponId  优惠券id
     * @param memberId  会员id
     * @param useStatus 0->未使用；1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) return;
        //查询第一张优惠券
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        example.createCriteria().andMemberIdEqualTo(memberId)
                .andCouponIdEqualTo(couponId).andUseStatusEqualTo(useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }

    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            //原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        //总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 获取可用积分抵扣金额
     *
     * @param useIntegration 使用的积分数量
     * @param totalAmount    订单总金额
     * @param currentMember  使用的用户
     * @param hasCoupon      是否已经使用优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        //判断用户是否有这么多积分
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        //根据积分使用规则判断是否可用
        //是否可与优惠券共用
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            //不可与优惠券共用
            return zeroAmount;
        }
        //是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        //是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * 对优惠券优惠进行处理
     *
     * @param orderItemList       order_item列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            //全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            //指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            //指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     *
     * @param orderItemList 可用优惠券的下单商品商品
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            //(商品价格/可用商品总价)*优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关系的下单商品
     *
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->相关分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param cartPromotionItemList 购物车优惠列表
     * @param couponId              使用优惠券id
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
                return couponHistoryDetail;
            }
        }
        return null;
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

//    /**
//     * 锁定下单商品的所有库存
//     */
//    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
//        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
//            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartPromotionItem.getProductSkuId());
//            skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
//            skuStockMapper.updateByPrimaryKeySelective(skuStock);
//        }
//    }
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
            pmsSkuStockMapper.batchUpdate(cart.getQuantity(),cart.getProductSkuId());
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
        cartItemMapper.removeByIds(ids);
//        cartItemService.removeByIds(ids);
    }
    /**
     * 判断下单商品是否都有库存
     */
//    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
//        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
//            if (cartPromotionItem.getRealStock()==null||cartPromotionItem.getRealStock() <= 0) {
//                return false;
//            }
//        }
//        return true;
//    }
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
    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
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
        UmsMemberReceiveAddress address = umsMemberReceiveAddressMapper.getOne(currentMember.getId(),paramDTO.getMemberReceiveAddressId());
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
}
