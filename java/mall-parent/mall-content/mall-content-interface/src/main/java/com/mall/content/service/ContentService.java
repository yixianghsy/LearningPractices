package com.mall.content.service;

import com.mall.mapper.pojo.TbContent;
import com.mall.utils.E3Result;

import java.util.List;

public interface ContentService {
    E3Result addContent(TbContent tbContent);
    List<TbContent> getContentListByCid(Long iid);
}
