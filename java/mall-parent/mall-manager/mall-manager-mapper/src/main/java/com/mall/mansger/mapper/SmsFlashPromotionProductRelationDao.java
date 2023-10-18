package com.mall.mansger.mapper;

import com.mall.mansger.dto.SmsFlashPromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 限时购商品关系管理自定义Dao
 * Created by macro on 2018/11/16.
 */
public interface SmsFlashPromotionProductRelationDao {
    /**
     * 获取限时购及相关商品信息
     */
    List<SmsFlashPromotionProduct> getList(@Param("flashPromotionId") Long flashPromotionId, @Param("flashPromotionSessionId") Long flashPromotionSessionId);
}
