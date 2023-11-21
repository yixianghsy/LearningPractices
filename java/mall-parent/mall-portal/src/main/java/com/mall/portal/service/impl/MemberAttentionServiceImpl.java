package com.mall.portal.service.impl;

import com.mall.portal.domain.MemberBrandAttention;
import com.mall.portal.repository.MemberBrandAttentionRepository;
import com.mall.portal.service.MemberAttentionService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.model.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 会员关注Service实现类
 * Created on 2018/8/2.
 */
@Service
public class MemberAttentionServiceImpl implements MemberAttentionService {

    @Autowired
    private MemberBrandAttentionRepository memberBrandAttentionRepository;
    @Autowired
    private UmsMemberService memberService;
    @Override
    public int add(MemberBrandAttention memberBrandAttention) {
        int count = 0;
        MemberBrandAttention findAttention = memberBrandAttentionRepository.findByMemberIdAndBrandId(memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            memberBrandAttentionRepository.save(memberBrandAttention);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long memberId, Long brandId) {
        return memberBrandAttentionRepository.deleteByMemberIdAndBrandId(memberId,brandId);
    }

    @Override
    public List<MemberBrandAttention> list(Long memberId) {
        return memberBrandAttentionRepository.findByMemberId(memberId);
    }

    @Override
    public MemberBrandAttention detail(Long brandId) {
        UmsMember member = memberService.getCurrentMember();
        return memberBrandAttentionRepository.findByMemberIdAndBrandId(member.getId(), brandId);
    }

    @Override
    public void clear() {
        UmsMember member = memberService.getCurrentMember();
        memberBrandAttentionRepository.deleteAllByMemberId(member.getId());
    }
}
