package com.mall.portal.service;

import com.mall.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 会员收藏Service
 * Created on 2018/8/2.
 */
public interface MemberCollectionService {
    int addProduct(MemberProductCollection productCollection);

    int deleteProduct(Long memberId, Long productId);

    List<MemberProductCollection> listProduct(Long memberId);

    /**
     * 分页查询收藏
     */
    Page<MemberProductCollection> list(Integer pageNum, Integer pageSize);

    /**
     * 查看收藏详情
     */
    MemberProductCollection detail(Long productId);

    /**
     * 清空收藏
     */
    void clear();
}
