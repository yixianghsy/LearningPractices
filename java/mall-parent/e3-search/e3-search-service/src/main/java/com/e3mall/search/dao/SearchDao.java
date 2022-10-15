package com.e3mall.search.dao;


//import com.e3mall.search.Utit.ElasticsearchUtil;
import com.e3mall.pojo.EsPage;
import com.e3mall.pojo.SearchItem;
import com.e3mall.pojo.SearchResult;
import com.e3mall.utils.ListUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 * <p>Title: SearchDao</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Repository
public class SearchDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDao.class);
    /**
     *
     * @param query  查询字段
     * @return
     * @throws Exception
     */
    //已经废弃
    public SearchResult search(QueryBuilder query ) throws Exception {
        EsPage esPage =null;
//        List<Map<String, Object>> recordList = esPage.getRecordList();
//        //数据要先遍历出来，如果通用可以把方法封装，
//        List<SearchItem> itemList = (List<SearchItem>) ListUtil.listMapToListBean(recordList, SearchItem.class);
        SearchResult result = null;
        //数据
//        result.setRecordCount(esPage.getRecordCount());
//        //总记录数不是总行数
//        result.setItemList(itemList);
        return result;
    }
}
