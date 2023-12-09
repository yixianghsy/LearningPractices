package com.mall.order.mapper;
import com.mall.order.model.OmsCartItem;
import com.mall.order.model.OmsCartItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OmsCartItemMapper {
    long countByExample(OmsCartItemExample example);

    int deleteByExample(OmsCartItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsCartItem record);

    int insertSelective(OmsCartItem record);

    List<OmsCartItem> selectByExample(OmsCartItemExample example);

    OmsCartItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OmsCartItem record, @Param("example") OmsCartItemExample example);

    int updateByExample(@Param("record") OmsCartItem record, @Param("example") OmsCartItemExample example);

    int updateByPrimaryKeySelective(OmsCartItem record);

    int updateByPrimaryKey(OmsCartItem record);

    /**
     * 更新购物车数量
     * @param id
     * @return
     */
    List<Map<String, Object>> selectMaps(Long id);
}