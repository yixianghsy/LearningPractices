package com.e3mall.mansger.service;
import com.e3mall.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
      List<EasyUITreeNode>  getItemCatlist(long parentId);
      String getListDome(int i);
}
