package com.mall.portal.service.impl;

import com.mall.portal.service.UmsMemberReceiveAddressService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.mapper.UmsMemberReceiveAddressMapper;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.model.UmsMemberReceiveAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 用户地址管理Service实现类
 * Created on 2018/8/28.
 */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;
    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address);
    }

    @Override
    public int delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.updateByExampleSelective(address,example);
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        return addressMapper.selectByExample(example);
    }

    @Override
    public List<UmsMemberReceiveAddress> list(Long id) {
        /**
         * SELECT id,member_id,name,phone_number,default_status,post_code,province,
         * city,region,detail_address FROM ums_member_receive_address WHERE (member_id = ?)
         */
        UmsMemberReceiveAddressExample  example  = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(id);
        return umsMemberReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(addressList)){
            return addressList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsMemberReceiveAddress> listByMemberId() {
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberService.getCurrentMember().getId());
        return addressMapper.selectByExample(example);
    }
}
