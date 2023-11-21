package com.mall.portal.repository;


import com.mall.portal.domain.MemberBrandAttention;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 会员关注Repository
 * Created on 2018/8/2.
 */
public interface MemberBrandAttentionRepository extends MongoRepository<MemberBrandAttention,String> {
    /**
     * 根据会员ID和品牌ID查找记录
     */
    MemberBrandAttention findByMemberIdAndBrandId(Long memberId, Long brandId);
    /**
     * 根据会员ID和品牌ID删除记录
     */
    int deleteByMemberIdAndBrandId(Long memberId,Long brandId);
    /**
     * 根据会员ID分页查找记录
     */
    List<MemberBrandAttention> findByMemberId(Long memberId);

    /**
     * 根据会员ID删除记录
     */
    void deleteAllByMemberId(Long memberId);
}
