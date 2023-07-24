package com.mall.item.service;





import com.mall.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCatlist(long parentId);
    String getListDome(int i);
}
