package com.mall.content.service;

import com.mall.modules.content.TbContent;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;

import java.util.List;

public interface ContentService {
    E3Result addContent(TbContent content);
    EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows);
    List<TbContent> getContentList(Long cid);
}
