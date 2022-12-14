package com.tulingxueyuan.mall.modules.oms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tulingxueyuan.mall.common.api.ResultCode;
import com.tulingxueyuan.mall.common.exception.ApiException;
import com.tulingxueyuan.mall.common.service.RedisService;
import com.tulingxueyuan.mall.modules.oms.mapper.OmsCartItemMapper;
import com.tulingxueyuan.mall.modules.oms.model.OmsCartItem;
import com.tulingxueyuan.mall.modules.oms.model.OmsOrderSetting;
import com.tulingxueyuan.mall.modules.oms.service.OmsCartItemService;
import com.tulingxueyuan.mall.modules.oms.service.OmsOrderSettingService;
import com.tulingxueyuan.mall.modules.pms.model.PmsProduct;
import com.tulingxueyuan.mall.modules.pms.model.PmsSkuStock;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductService;
import com.tulingxueyuan.mall.modules.pms.service.PmsSkuStockService;
import com.tulingxueyuan.mall.modules.ums.model.UmsMemberReceiveAddress;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberReceiveAddressService;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberService;
import com.tulingxueyuan.mall.component.TradePayProp;
import com.tulingxueyuan.mall.dto.*;
import com.tulingxueyuan.mall.modules.oms.mapper.OmsOrderMapper;
import com.tulingxueyuan.mall.modules.oms.model.OmsOrder;
import com.tulingxueyuan.mall.modules.oms.model.OmsOrderItem;
import com.tulingxueyuan.mall.modules.oms.service.OmsOrderItemService;
import com.tulingxueyuan.mall.modules.oms.service.OmsOrderService;
import com.tulingxueyuan.mall.modules.ums.model.UmsMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-21
 */
@Service
@Slf4j
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {


    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private PmsProductService productService;
    @Autowired
    private UmsMemberReceiveAddressService addressService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    OmsCartItemMapper cartItemMapper;
    @Autowired
    RedisService redisService;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Autowired
    OmsOrderItemService orderItemService;
    @Autowired
    PmsSkuStockService skuStockService;
    @Autowired
    OmsOrderSettingService orderSettingService;

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
        QueryWrapper<UmsMemberReceiveAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId());
        List<UmsMemberReceiveAddress> list = addressService.list(queryWrapper);
        confirmOrderDTO.setAddressList(list);

        return confirmOrderDTO;
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
     * 下单
     * 1. 判断库存（如果没有库存直接提示）
     *      没有库存id 只有购物车id，根据购物车id查询所有购物车信息
     * 2.保存订单主表oms_order信息  订单号
     * 3.保存订单详情表oms_order_item( 购物车转移）
     * 4.锁定库存（如果用户超过10分钟没有支付-恢复库存）
     * 5.删除对应购物车
     * 先不考虑并发
     * @param paramDTO
     * @return
     */
    @Override
    @Transactional
    public OmsOrder generateOrder(OrderParamDTO paramDTO) {

        // 根据购物车id 查询真实库存
        UmsMember currentMember = memberService.getCurrentMember();
        QueryWrapper<OmsCartItem> queryWrapper = new QueryWrapper<>();

        // 防止用户篡改
        queryWrapper.lambda().eq(OmsCartItem::getMemberId,currentMember.getId())
                .in(OmsCartItem::getId,paramDTO.getItemIds());
        // 根据购物车id查询所有购物车信息
        List<CartItemStockDTO> cartItemStockByIds = cartItemMapper.getCartItemStockByIds(queryWrapper);

        // 1. 判断库存（如果没有库存直接提示）
        // 获取库存不足的名称 如果productName为空说明库存未超出
        String productName = hasStock(cartItemStockByIds);
        if(StrUtil.isNotEmpty(productName)){
            throw new ApiException("您的手速过慢，"+productName+"已被别人买走");
        }

        // 如果有库存就进行下单操作：
        //2.保存订单主表oms_order信息  订单号
        OmsOrder omsOrder = newOrder(paramDTO, currentMember, cartItemStockByIds);
        this.save(omsOrder);

        // 3.保存订单详情表oms_order_item( 购物车转移）
        List<OmsOrderItem> list=newOrderItems(omsOrder,cartItemStockByIds);
        orderItemService.saveBatch(list);

        // 4.锁定库存
        lockStock(cartItemStockByIds);

        // 5.删除对应购物车
        removeCartItem(cartItemStockByIds);

        return omsOrder;
    }

    /**
     *
     * 读取下单成功订单详情
     * @param id
     * @return
     */
    @Override
    public OrderDetailDTO getOrderDetail(Long id) {
        return this.baseMapper.getOrderDetail(id);
    }

    /**
     * 自动取消规定时间段内未支付的订单
     */
    @Override
    public void cancelOverTimeOrder() {
        // 1.获取规定的时间
        OmsOrderSetting orderSetting = orderSettingService.getById(1L);
        // 普通订单的超时分钟
        Integer overtime = orderSetting.getNormalOrderOvertime();

        // 获取当前时间的指定时间之前
        DateTime offset = DateUtil.offset(new Date(), DateField.MINUTE, -overtime);

        // 2. 获取超过规定时间未支付的订单
        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OmsOrder::getStatus,0)  //未支付
                .le(OmsOrder::getCreateTime,offset);  //是否超时

        // 所有超时未支付的订单
        List<OmsOrder> list = this.list(queryWrapper);

        if(CollectionUtil.isEmpty(list)){
            log.warn("暂无超时订单");
            return;
        }
        // 订单的id 用于获取订单详情
        List<Long> orderIds=new ArrayList<>();
        for (OmsOrder omsOrder : list) {
            omsOrder.setStatus(4); // 设置订单关闭
            omsOrder.setModifyTime(new Date());
            orderIds.add(omsOrder.getId());
        }

        // 3. 改变状态：取消
        this.updateBatchById(list);


        // 4. 归还锁定库存
        // 4.1 获取订单详情
        QueryWrapper<OmsOrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.lambda().in(OmsOrderItem::getOrderId,orderIds);
        List<OmsOrderItem> itemList = orderItemService.list(itemQueryWrapper);// in (订单id)
        // 循环归还库存
        for (OmsOrderItem omsOrderItem : itemList) {
            // 归还的数量
            Integer productQuantity = omsOrderItem.getProductQuantity();
            // skuid
            Long productSkuId = omsOrderItem.getProductSkuId();
            UpdateWrapper<PmsSkuStock> stockUpdateWrapper = new UpdateWrapper<>();
            stockUpdateWrapper.setSql("lock_stock=lock_stock-"+productQuantity)
                    .lambda()
                    .eq(PmsSkuStock::getId,productSkuId);
            skuStockService.update(stockUpdateWrapper);
        }

    }

    @Override
    public IPage<OrderListDTO> getMyOrders(Integer pageSize, Integer pageNum) {

        Page page=new Page(pageNum,pageSize);
        return this.baseMapper.getMyOrders(page,memberService.getCurrentMember().getId());
    }

    @Override
    public void paySuccess(Long orderId, Integer payType) {
        /**
         * 1更新订单状态和支付方式、支付时间        唯一标识
         * 2.清除锁定库存，扣除实际库存
         * 3.删除二维码
         */
        //更新订单状态和支付方式、支付时间
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setStatus(1); // 待发货
        omsOrder.setPayType(payType);
        omsOrder.setPaymentTime(new Date());
        omsOrder.setId(orderId);

        UpdateWrapper<OmsOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(OmsOrder::getStatus,omsOrder.getStatus())
                .set(OmsOrder::getPayType,omsOrder.getPayType())
                .set(OmsOrder::getPaymentTime,omsOrder.getPaymentTime())
                .eq(OmsOrder::getId,omsOrder.getId());
        boolean update = this.update(updateWrapper);

        if(!update){
            throw  new ApiException("订单支付成功：更新失败！");
        }

        //清除锁定库存，扣除实际库存
        QueryWrapper<OmsOrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.lambda().eq(OmsOrderItem::getOrderId,omsOrder.getId());
        List<OmsOrderItem> list = orderItemService.list(itemQueryWrapper);
        // 所有需要更新库存的id
        for (OmsOrderItem omsOrderItem : list) {

            UpdateWrapper<PmsSkuStock> skuUpdateWrapper = new UpdateWrapper<>();
            skuUpdateWrapper.setSql("stock=stock-"+omsOrderItem.getProductQuantity())
                    .setSql("lock_stock=lock_stock-"+omsOrderItem.getProductQuantity())
                    .lambda().eq(PmsSkuStock::getId,omsOrderItem.getProductSkuId());
            skuStockService.update(skuUpdateWrapper);
        }

        //删除二维码
        // 需要修改为运行机器上的路径
        String fileName= String.format("/qr-%s.png", orderId);
        String filePath =tradePayProp.getStorePath()+fileName;
        // 如果二维码存在
        if(FileUtil.exist(filePath) && FileUtil.isFile(filePath)){
            // 删除
            FileUtil.del(filePath);
        }

    }

    @Autowired
    TradePayProp tradePayProp;

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
    }

    /**
     * 锁定库存  把当前的购买数累加到sku lock_stock中
     * @param cartItemStockByIds
     */
    private void lockStock(List<CartItemStockDTO> cartItemStockByIds) {
        for (CartItemStockDTO cart : cartItemStockByIds) {
            UpdateWrapper<PmsSkuStock> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("lock_stock=lock_stock+"+cart.getQuantity())
                    .lambda()
                    .eq(PmsSkuStock::getId,cart.getProductSkuId());

            skuStockService.update(updateWrapper);
        }
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
        QueryWrapper<UmsMemberReceiveAddress> addressQueryWrapper = new QueryWrapper<UmsMemberReceiveAddress>();
        addressQueryWrapper.lambda().eq(UmsMemberReceiveAddress::getMemberId,currentMember.getId())
                .eq(UmsMemberReceiveAddress::getId,paramDTO.getMemberReceiveAddressId());
        UmsMemberReceiveAddress address = addressService.getOne(addressQueryWrapper);
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
}
