package com.mall.portal.repository;


import com.mall.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 商品收藏Repository
 * Created on 2018/8/2.
 */
public interface MemberProductCollectionRepository extends MongoRepository<MemberProductCollection,String> {
    MemberProductCollection findByMemberIdAndProductId(Long memberId, Long productId);
    int deleteByMemberIdAndProductId(Long memberId,Long productId);
    List<MemberProductCollection> findByMemberId(Long memberId);
    /**
     * 根据会员ID分页查询记录
     */
    Page<MemberProductCollection> findByMemberId(Long memberId, Pageable pageable);
    /**
     * 根据会员ID删除记录
     */
    void deleteAllByMemberId(Long memberId);
}
