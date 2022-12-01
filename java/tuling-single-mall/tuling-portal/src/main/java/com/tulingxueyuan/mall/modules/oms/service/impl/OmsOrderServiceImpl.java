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
//import com.tulingxueyuan.mall.component.TradePayProp;
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

    @Override
    public ConfirmOrderDTO generateConfirmOrder(List<Long> ids) {
        return null;
    }

    @Override
    public OmsOrder generateOrder(OrderParamDTO paramDTO) {
        return null;
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long id) {
        return null;
    }

    @Override
    public void cancelOverTimeOrder() {

    }

    @Override
    public IPage<OrderListDTO> getMyOrders(Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public void paySuccess(Long orderId, Integer payType) {

    }
}
