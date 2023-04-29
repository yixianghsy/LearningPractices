package com.mall.search.mapper;

import com.mall.pojo.SearchItem;

import java.util.List;


public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
