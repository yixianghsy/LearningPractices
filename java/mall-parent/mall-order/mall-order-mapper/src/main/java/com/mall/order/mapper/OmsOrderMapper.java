package com.mall.order.mapper;
import com.mall.order.dto.OmsOrderDetail;
import com.mall.order.dto.OrderDetailDTO;
import com.mall.order.model.OmsOrder;
import com.mall.order.model.OmsOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderMapper {
    long countByExample(OmsOrderExample example);

    int deleteByExample(OmsOrderExample example);

    int deleteByPrimaryKey(Long id);

    Long insert(OmsOrder record);

    int insertSelective(OmsOrder record);

    List<OmsOrder> selectByExample(OmsOrderExample example);

    OmsOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OmsOrder record, @Param("example") OmsOrderExample example);

    int updateByExample(@Param("record") OmsOrder record, @Param("example") OmsOrderExample example);

    int updateByPrimaryKeySelective(OmsOrder record);

    int updateByPrimaryKey(OmsOrder record);
    OrderDetailDTO getOrderDetail(Long id);

    /**
     * 获取订单及下单商品详情
     */
    OmsOrderDetail getDetail(@Param("orderId") Long orderId);

    /**
     * 查询会员的订单
     * @param memberId
     *      会员ID
     * @param status
     *      订单状态
     * @return
     */
    List<OmsOrderDetail> findMemberOrderList(@Param("memberId") Long memberId,@Param("status") Integer status);
}