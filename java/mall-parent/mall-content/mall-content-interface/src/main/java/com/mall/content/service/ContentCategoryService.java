package com.mall.content.service;

import com.mall.pojo.EasyUITreeNode;
import com.mall.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCategoryList(Long parentId);
    E3Result addContentCategory(long parentId, String name);
}
