package com.tulingxueyuan.mall.modules.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tulingxueyuan.mall.modules.ums.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员收货地址表 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
public interface UmsMemberReceiveAddressService extends IService<UmsMemberReceiveAddress> {

    Boolean add(UmsMemberReceiveAddress address);

    Boolean edit(UmsMemberReceiveAddress address);

    Boolean delete(Long id);

    List<UmsMemberReceiveAddress> listByMemberId();
}
