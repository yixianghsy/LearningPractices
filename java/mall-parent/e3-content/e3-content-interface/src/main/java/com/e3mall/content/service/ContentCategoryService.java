package com.e3mall.content.service;

import com.e3mall.pojo.EasyUITreeNode;
import com.e3mall.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCatList(long parentId);
    E3Result addContentCategory(long parentId, String name);
}
