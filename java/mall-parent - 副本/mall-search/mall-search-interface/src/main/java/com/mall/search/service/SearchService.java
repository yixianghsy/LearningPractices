package com.mall.search.service;

import com.mall.pojo.SearchResult;

public interface SearchService {
    SearchResult search(int startPage, int pageSize, String query)  throws Exception;
}
