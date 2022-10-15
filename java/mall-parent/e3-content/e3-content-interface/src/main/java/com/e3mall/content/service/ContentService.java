package com.e3mall.content.service;

import com.e3mall.mapper.pojo.TbContent;
import com.e3mall.utils.E3Result;

import java.util.List;

public interface ContentService {
    E3Result addContent(TbContent tbContent);
    List<TbContent> getContentListByCid(Long iid);
}
