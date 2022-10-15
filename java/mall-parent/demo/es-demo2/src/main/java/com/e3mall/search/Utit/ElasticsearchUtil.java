package com.e3mall.search.Utit;

import com.alibaba.fastjson.JSON;

import com.e3mall.search.pojo.EsEntity;
import com.e3mall.search.pojo.EsPage;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ElasticsearchUtil<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchUtil.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    /**
     * spring容器初始化的时候执行该方法
     *
     */
    //TODO 如果类比较多，这个初始化一次就好，或在做成工具类
    @PostConstruct
    public void init() {
        client = this.restHighLevelClient;
    }

    /**
     * 判断索引是否存在     *
     *
     * @param index 索引，类似数据库
     * @return boolean
     * @auther: LHL
     */
    public static boolean isIndexExist(String index) {
        boolean exists = false;
        try {
            exists = client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exists) {
            LOGGER.info("Index [" + index + "] is exist!");
        } else {
            LOGGER.info("Index [" + index + "] is not exist!");
        }
        return exists;
    }

    /**
     * 创建索引以及映射mapping，并给索引某些字段指定iK分词，以后向该索引中查询时，就会用ik分词。
     *
     * @param: indexName  索引，类似数据库
     * @return: boolean
     * @auther: LHL
     */
    public static boolean createIndex(String indexName) {
        if (!isIndexExist(indexName)) {
            LOGGER.info("Index is not exits!");
        }
        CreateIndexResponse createIndexResponse = null;
        try {
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> propertie = new HashMap<>();
            // 类型
            propertie.put("type", "text");
            // 分词器 ik_max_word  ik_smart  ik_max_word  standard
            propertie.put("analyzer", "ik_max_word");
            properties.put("title", propertie);

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject()
                    .startObject("mappings")

                    .field("properties", properties)
                    .endObject()
                    .endObject()
                    .startObject("settings")
                    //分片数
                    .field("number_of_shards", 3)
                    //副本数
                    .field("number_of_replicas", 1)
                    .endObject()
                    .endObject();
            CreateIndexRequest request = new CreateIndexRequest(indexName).source(builder);
            //设置创建索引超时2分钟
            request.setTimeout(TimeValue.timeValueMinutes(2));
            createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createIndexResponse.isAcknowledged();
    }


}