package com.e3mall.search.service.impl;
import com.e3mall.search.Utit.EsUtil;
import com.e3mall.search.service.SearchService;
import com.e3mall.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;

import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 商品搜索Service
 * <p>Title: SearchServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private EsUtil esUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDao.class);

    /**
     *
     * @param query  查询条件
     * @param startPage     当前页
     * @param pageSize      每页显示条数
     * @return
     * @throws Exception
     */
    @Override
    public SearchResult search(int startPage, int pageSize, String query) throws Exception {
        SearchResult esPage = esUtil.searchDataPage(startPage, pageSize, query);
        System.out.println(esPage.getItemList().get(0));
        return esPage;
    }
}
