package com.mall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.mall.api.ResultCode;
import com.mall.exception.ApiException;
import com.mall.mansger.model.PmsProduct;
import com.mall.order.mapper.OmsOrderItemMapper;
import com.mall.order.mapper.OmsOrderMapper;
import com.mall.order.model.*;
import com.mall.order.service.OmsOrderSettingService;
import com.mall.portal.dto.ConfirmOrderDTO;
import com.mall.portal.dto.OrderDetailDTO;
import com.mall.portal.dto.OrderParamDTO;
import com.mall.portal.service.OmsCartItemService;
import com.mall.portal.service.OmsOrderService;
import com.mall.portal.service.PmsProductService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.mapper.UmsMemberReceiveAddressMapper;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.model.UmsMemberReceiveAddressExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private PmsProductService productService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;
    @Reference
    OmsOrderSettingService orderSettingService;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
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
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        List<UmsMemberReceiveAddress> list = addressMapper.selectByExample(example);
        confirmOrderDTO.setAddressList(list);
        return confirmOrderDTO;
    }

    @Override
    public OmsOrder generateOrder(OrderParamDTO paramDTO) {
        return null;
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long id) {
        return null;
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

        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andStatusEqualTo(0).//未支付
                andCreateTimeLessThanOrEqualTo(offset);//是否超时

        // 所有超时未支付的订单
        List<OmsOrder> list = orderMapper.selectByExample(omsOrderExample);


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
        // TODO 根据ID批量更新
        // 3. 改变状态：取消
//        this.updateBatchById(list);


        // 4. 归还锁定库存
        // 4.1 获取订单详情
        OmsOrderItemExample itemQueryExample = new OmsOrderItemExample();
        itemQueryExample.createCriteria().andOrderIdIn(orderIds);
        List<OmsOrderItem> itemList = omsOrderItemMapper.selectByExample(itemQueryExample);// in (订单id)
        // 循环归还库存
        for (OmsOrderItem omsOrderItem : itemList) {
            // 归还的数量
            Integer productQuantity = omsOrderItem.getProductQuantity();
            // skuid
            Long productSkuId = omsOrderItem.getProductSkuId();
            // TODO  手写更新语句
//            update  pms_sku_stock  set lock_stock=lock_stock-"+productQuantity" where   id= "productSkuId";
//            UpdateWrapper<PmsSkuStock> stockUpdateWrapper = new UpdateWrapper<>();
//            stockUpdateWrapper.setSql("lock_stock=lock_stock-"+productQuantity)
//                    .lambda()
//                    .eq(PmsSkuStock::getId,productSkuId);
//            skuStockService.update(stockUpdateWrapper);
        }
    }

    @Override
    public void paySuccess(Long orderId, Integer payType) {

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
}
