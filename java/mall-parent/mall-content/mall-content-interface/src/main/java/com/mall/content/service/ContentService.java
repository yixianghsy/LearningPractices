package com.mall.content.service;

import com.mall.pojo.EasyUIDataGridResult;

public interface ContentService {

    EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows);
}
