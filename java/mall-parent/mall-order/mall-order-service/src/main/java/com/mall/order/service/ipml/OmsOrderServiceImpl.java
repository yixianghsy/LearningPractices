package com.mall.order.service.ipml;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageHelper;

import com.mall.exception.ApiException;
import com.mall.mansger.mapper.PmsSkuStockMapper;
import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.model.PmsSkuStockExample;
import com.mall.mansger.service.PmsSkuStockService;
import com.mall.order.dto.*;
import com.mall.order.mapper.*;
import com.mall.order.model.*;
import com.mall.order.service.OmsOrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理Service实现类
 * Created by macro on 2018/10/11.
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderDao orderDao;
    @Autowired
    private OmsOrderOperateHistoryDao orderOperateHistoryDao;
    @Autowired
    private OmsOrderOperateHistoryMapper orderOperateHistoryMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Reference
    private PmsSkuStockService pmsSkuStockService;
//    @Autowired
//    private TradePayProp tradePayProp;
    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return orderDao.getList(queryParam);
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        //批量发货
        int count = orderDao.delivery(deliveryParamList);
        //添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(operateHistoryList);
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder record = new OmsOrder();
        record.setStatus(4);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        int count = orderMapper.updateByExampleSelective(record, example);
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(4);
            history.setNote("订单关闭:"+note);
            return history;
        }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        return orderMapper.updateByExampleSelective(record, example);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDao.getDetail(id);
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(status);
        history.setNote("修改备注信息："+note);
        orderOperateHistoryMapper.insert(history);
        return count;
    }
    //  TODO 代码未完善
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
        OmsOrderExample omsOrderExample = new OmsOrderExample();
        omsOrderExample.createCriteria().andStatusEqualTo(0).andIdEqualTo(orderId);
        int update = orderMapper.updateByExampleSelective(omsOrder, omsOrderExample);
//        omsOrder.setId(orderId);
//        UpdateWrapper<OmsOrder> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.lambda().set(OmsOrder::getStatus,omsOrder.getStatus())
//                .set(OmsOrder::getPayType,omsOrder.getPayType())
//                .set(OmsOrder::getPaymentTime,omsOrder.getPaymentTime())
//                .eq(OmsOrder::getId,omsOrder.getId());
//        boolean update = this.update(updateWrapper);
        //==0没有更新成功
        if(update==0){
            throw  new ApiException("订单支付成功：更新失败！");
        }

        //清除锁定库存，扣除实际库存
        OmsOrderItemExample omsOrderItemExample = new OmsOrderItemExample();
        omsOrderItemExample.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> list = omsOrderItemMapper.selectByExample(omsOrderItemExample);
        for (OmsOrderItem omsOrderItem : list) {

            pmsSkuStockService.minusUpdate(
                    omsOrderItem.getProductQuantity(),
                    omsOrderItem.getProductQuantity(),
                    omsOrderItem.getProductSkuId()
            );
        }

        //删除二维码
        // 需要修改为运行机器上的路径
        String fileName= String.format("/qr-%s.png", orderId);
        String filePath ="E:/Temp/qr-code"+fileName;
        // 如果二维码存在
        if(FileUtil.exist(filePath) && FileUtil.isFile(filePath)){
            // 删除
            FileUtil.del(filePath);
        }
    }
}
