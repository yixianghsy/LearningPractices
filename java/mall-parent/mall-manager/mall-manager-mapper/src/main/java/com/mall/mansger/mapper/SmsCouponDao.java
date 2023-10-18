package com.mall.mansger.mapper;

import com.mall.mansger.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券管理自定义Dao
 * Created by macro on 2018/8/29.
 */
public interface SmsCouponDao {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(@Param("id") Long id);
}
