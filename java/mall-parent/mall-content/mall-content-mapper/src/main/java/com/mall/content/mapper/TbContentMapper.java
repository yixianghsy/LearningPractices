package com.mall.content.mapper;

import com.mall.modules.content.TbContent;
import com.mall.modules.content.TbContentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbContentMapper {
    int countByExample(TbContentExample example);

    int deleteByExample(TbContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbContent record);

    int insertSelective(TbContent record);

    List<TbContent> selectByExampleWithBLOBs(TbContentExample example);

    List<TbContent> selectByExample(TbContentExample example);

    TbContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByExampleWithBLOBs(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByExample(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByPrimaryKeySelective(TbContent record);

    int updateByPrimaryKeyWithBLOBs(TbContent record);

    int updateByPrimaryKey(TbContent record);

    List<TbContent> getContentListByCategoryId(Long categoryId);

    List<TbContent> getAllContentList();

    void insertContent(TbContent tbContent);
}