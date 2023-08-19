package com.mall.content.mapper;


import com.mall.modules.content.TbContentCategory;
import java.util.List;

public interface TbContentCategoryMapper {

    List<TbContentCategory> selectTbContentCatsByParentId(Long parentId);

    void insertCategory(TbContentCategory contentCategory);

    TbContentCategory selectTbContentCatById(Long id);

    void updateContentCategoryById(TbContentCategory parentContentCategory);


}