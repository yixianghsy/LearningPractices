package com.tulingxueyuan.mall.modules.oms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tulingxueyuan.mall.dto.CartItemStockDTO;
import com.tulingxueyuan.mall.modules.oms.model.OmsCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
public interface OmsCartItemMapper extends BaseMapper<OmsCartItem> {

    List<CartItemStockDTO> getCartItemStock(Long id);

    /**
     *  在自定义xml查询 结合mp的Wrapper 来使用
     * @param ew
     * @return
     */
    List<CartItemStockDTO> getCartItemStockByIds(@Param(Constants.WRAPPER)Wrapper ew);

}
