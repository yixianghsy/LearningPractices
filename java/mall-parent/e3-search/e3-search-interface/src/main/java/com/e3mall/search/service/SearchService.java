package com.e3mall.search.service;


import com.e3mall.pojo.SearchResult;

public interface SearchService {

	SearchResult search(int startPage, int pageSize, String query)  throws Exception;
}
