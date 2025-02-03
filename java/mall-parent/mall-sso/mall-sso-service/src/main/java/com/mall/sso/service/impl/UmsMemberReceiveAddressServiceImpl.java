package com.mall.sso.service.impl;

import com.mall.sso.mapper.UmsMemberReceiveAddressMapper;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberReceiveAddress;
import com.mall.sso.model.UmsMemberReceiveAddressExample;
import com.mall.sso.service.UmsMemberReceiveAddressService;
import com.mall.sso.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;
    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Override
    public Boolean add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address)>0;
    }

    @Override
    public Boolean edit(UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(address.getId());
        return addressMapper.updateByExampleSelective(address,example)>0;
    }

    @Override
    public Boolean delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example)>0;
    }

    @Override
    public List<UmsMemberReceiveAddress> listByMemberId() {
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberService.getCurrentMember().getId());
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
    public UmsMemberReceiveAddress getOne(Long id, Long memberReceiveAddressId) {
        return umsMemberReceiveAddressMapper.getOne(id,memberReceiveAddressId);
    }
}
