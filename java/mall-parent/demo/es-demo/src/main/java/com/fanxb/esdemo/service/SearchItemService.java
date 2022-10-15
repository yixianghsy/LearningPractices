package com.fanxb.esdemo.service;

import com.fanxb.esdemo.entity.Book;
import com.fanxb.esdemo.entity.EsEntity;
import com.fanxb.esdemo.entity.SearchItem;
import com.fanxb.esdemo.util.EsUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Service
public class SearchItemService {


    @Autowired
    private EsUtil esUtil;

    public void putOne1( SearchItem searchItem) {
        EsEntity<SearchItem> entity = new EsEntity<>(searchItem.getId().toString(), searchItem);
        esUtil.insertOrUpdateOne(EsUtil.INDEX_NAME, entity);
    }


    /**
     * @param id 获取某一个
     */
    public SearchItem getById( String id) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(new TermQueryBuilder("id", id));
        List<SearchItem> res = esUtil.search(EsUtil.INDEX_NAME, builder, SearchItem.class);
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据关键词搜索某用户下的书
     *
     * @param content 关键词
     */
    @GetMapping("/search1")
    public List<SearchItem> searchByUserIdAndName(String id, String content) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery("id", id));
        boolQueryBuilder.must(QueryBuilders.matchQuery("image", content));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(10).query(boolQueryBuilder);
        return esUtil.search(EsUtil.INDEX_NAME, builder, SearchItem.class);

    }

    public void putOne(SearchItem book) {
        EsEntity<SearchItem> entity = new EsEntity<>(book.getId().toString(), book);
        esUtil.insertOrUpdateOne(EsUtil.INDEX_NAME, entity);
    }

}
