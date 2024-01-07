package com.mall.mansger.mapper;

import com.mall.mansger.model.PmsSkuStock;
import com.mall.mansger.model.PmsSkuStockExample;
import com.mall.order.dto.CartItemStockDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsSkuStockMapper {
    long countByExample(PmsSkuStockExample example);

    int deleteByExample(PmsSkuStockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsSkuStock record);

    int insertSelective(PmsSkuStock record);

    List<PmsSkuStock> selectByExample(PmsSkuStockExample example);

    PmsSkuStock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsSkuStock record, @Param("example") PmsSkuStockExample example);

    int updateByExample(@Param("record") PmsSkuStock record, @Param("example") PmsSkuStockExample example);

    int updateByPrimaryKeySelective(PmsSkuStock record);

    int updateByPrimaryKey(PmsSkuStock record);

    int batchUpdate(Integer quantity,Long productSkuId);

    int minusUpdate(@Param("stock") Integer stock,@Param("lowStock") Integer lowStock ,@Param("id") Long id);

}