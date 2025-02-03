package com.mall.sso.service;

import com.mall.sso.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员收货地址表 服务类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
public interface UmsMemberReceiveAddressService {

    Boolean add(UmsMemberReceiveAddress address);

    Boolean edit(UmsMemberReceiveAddress address);

    Boolean delete(Long id);

    List<UmsMemberReceiveAddress> listByMemberId();
    /**
     * 返回当前用户的收货地址
     */
    List<UmsMemberReceiveAddress> list(Long id);

    UmsMemberReceiveAddress getOne(Long id, Long memberReceiveAddressId);
}

