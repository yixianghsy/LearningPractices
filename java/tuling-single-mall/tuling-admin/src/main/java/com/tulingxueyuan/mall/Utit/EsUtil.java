package com.tulingxueyuan.mall.Utit;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tulingxueyuan.mall.common.pojo.SearchItem;
import com.tulingxueyuan.mall.common.pojo.SearchResult;
import com.tulingxueyuan.mall.dto.EsEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 类功能简述：
 * 类功能详述：
 *
 * @author fanxb
 * @date 2019/7/29 11:24
 */
@Component
public class EsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsUtil.class);

    @Value("${es.host}")
    public String host;
    @Value("${es.port}")
    public int port;
    @Value("${es.scheme}")
    public String scheme;
    /**
     * 索引名称
     */
    public static final String INDEX_NAME = "item_title";

    public static final String HIGHLIGH_FIELD  ="item_title";

    public static final String CREATE_INDEX = "{\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\":\"text\"\n" +
            "      },\n" +
            "      \"item_image\":{\n" +
            "        \"type\":\"text\"\n" +
            "      },\n" +
            "      \"item_sell_point\":{\n" +
            "        \"type\":\"text\"\n" +
            "      },\n" +
            "      \"item_price\":{\n" +
            "        \"type\":\"integer\"\n" +
            "      },\n" +
            "      \"item_title\":{\n" +
            "        \"type\":\"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"item_category_name\":{\n" +
            "        \"type\":\"text\",\n" +
            "        \"index\": true,\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      }\n" +
            "    }\n" +
            "  }";

    public static RestHighLevelClient client = null;

    /**
     * 初始化配置，
     */
    @PostConstruct
    public void init() {
        try {
            if (client != null) {
                client.close();
            }
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme)));
            if (this.indexExist(INDEX_NAME)) {
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
            request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
            //创建mapping
            request.mapping(CREATE_INDEX, XContentType.JSON);
            CreateIndexResponse res = client.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Description: 判断某个index是否存在
     *
     * @param index index名
     * @return boolean
     * @author fanxb
     * @date 2019/7/24 14:57
     */
    public boolean indexExist(String index) throws Exception {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * Description: 插入/更新一条记录
     *
     * @param index  index
     * @param entity 对象
     * @author fanxb
     * @date 2019/7/24 15:02
     */
    public void insertOrUpdateOne(String index, EsEntity entity) {
        IndexRequest request = new IndexRequest(index);
        request.id(entity.getId());
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 数据添加
     *
     * @param content   要增加的数据
     * @param indexName 索引，类似数据库
     * @param id        id
     * @return String
     * @auther: LHL
     */
    public  String addData(XContentBuilder content, String indexName, String id) {
        IndexResponse response = null;
        try {
            IndexRequest request = new IndexRequest(indexName).id(id).source(content);
            response = client.index(request, RequestOptions.DEFAULT);
            LOGGER.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getId();
    }
    /**
     * 批量添加数据
     *
     * @param list  要批量增加的数据
     * @param index 索引，类似数据库
     * @returnt
     * @auther: LHL
     */
    public void insertBatch(String index, List<EsEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(index).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));

        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Description: 批量删除
     *
     * @param index  index
     * @param idList 待删除列表
     * @author fanxb
     * @date 2019/7/25 14:24
     */
    public <T> void deleteBatch(String index, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 搜索
     *
     * @param index   index
     * @param builder 查询参数
     * @param c       结果类对象
     * @return java.util.ArrayList
     * @author fanxb
     * @date 2019/7/25 13:46
     */
    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 删除index
     *
     * @param index index
     * @return void
     * @author fanxb
     * @date 2019/7/26 11:30
     */
    public void deleteIndex(String index) {
        try {
            client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: delete by query
     *
     * @param index   index
     * @param builder builder
     * @author fanxb
     * @date 2019/7/26 15:16
     */
    public void deleteByQuery(String index, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用分词查询  高亮 排序 ,并分页
     *
     * 三个参数：关键字，当前页，每夜行数（startPage,paageSize,query)
     * index 固定
     *  highlightField 高亮字段固定
     *   sortField     抛弃
     *   fields        默认全部数据
     *
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @return 结果
     */
    public SearchResult searchDataPage(int startPage, int pageSize, String query) {
        TermQueryBuilder builder = QueryBuilders.termQuery(HIGHLIGH_FIELD, query);
        //索引
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置一个可选的超时，控制允许搜索的时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        if (StringUtils.isNotEmpty(HIGHLIGH_FIELD)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //设置前缀
            highlightBuilder.preTags("<em style=\"color:red\">");
            //设置后缀
            highlightBuilder.postTags("</em>");
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(HIGHLIGH_FIELD);
            //荧光笔类型
            highlightTitle.highlighterType("unified");
            // 设置高亮字段
            highlightBuilder.field(highlightTitle);
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // 设置是否按查询匹配度排序
        searchSourceBuilder.explain(true);
        if (startPage <= 0) {
            startPage = 0;
        }
        //如果 pageSize是10 那么startPage>9990 (10000-pagesize) 如果 20  那么 >9980 如果 50 那么>9950
        //深度分页  TODO
        if (startPage > (10000 - pageSize)) {
            searchSourceBuilder.query(builder);
            searchSourceBuilder
                    // .setScroll(TimeValue.timeValueMinutes(1))
                    .size(10000);
            //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
            LOGGER.info("\n{}", searchSourceBuilder);
            // 执行搜索,返回搜索响应信息
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //有多少条符合的
            long totalHits = searchResponse.getHits().getTotalHits().value;
            if (searchResponse.status().getStatus() == 8200) {
                //使用scrollId迭代查询
                List<Map<String, Object>> result = disposeScrollResult(searchResponse, HIGHLIGH_FIELD);
                List<Map<String, Object>> sourceList = result.stream().parallel().skip((startPage - 1 - (10000 / pageSize)) * pageSize).limit(pageSize).collect(Collectors.toList());
                return new SearchResult(startPage, pageSize, (int) totalHits, sourceList);
            }
        } else {//浅度分页
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.query(builder);
            /*MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("username", "pretty");
		matchQueryBuilder.fuzziness(Fuzziness.AUTO);//在匹配查询上启用模糊匹配
		matchQueryBuilder.prefixLength(3);//在匹配查询上设置前缀长度选项
		matchQueryBuilder.maxExpansions(10);//设置最大扩展选项以控制查询的模糊过程
		searchSourceBuilder.query(matchQueryBuilder);*/
            // 分页应用
            searchSourceBuilder
                    //设置from确定结果索引的选项以开始搜索。默认为0
                    // .from(startPage)
                    .from((startPage - 1) * pageSize)
                    //设置size确定要返回的搜索匹配数的选项。默认为10
                    .size(pageSize);
            //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
            LOGGER.info("\n{}", searchSourceBuilder);
            // 执行搜索,返回搜索响应信息
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //从返回结果中获取总记录书
            long totalHits = searchResponse.getHits().getTotalHits().value;
            //c从返回结果中获取长度
            long length = searchResponse.getHits().getHits().length;
            LOGGER.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
            //如果获取到数据状态为200，
            if (searchResponse.status().getStatus() == 200) {
                // 解析对象
                List<SearchItem>  sourceList = setSearchResponse(searchResponse, HIGHLIGH_FIELD);
                //可以看看只要自己需要的，比如totalHits, sourceList
                return new SearchResult(startPage, pageSize, (int) totalHits, sourceList);
            }
        }
        //否则返回null
        return null;
    }
    /**
     * 处理scroll结果
     *
     * @param: [response, highlightField]
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @auther: LHL
     */
    private static List<Map<String, Object>> disposeScrollResult(SearchResponse response, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<>();
        //使用scrollId迭代查询
        while (response.getHits().getHits().length > 0) {
            String scrollId = response.getScrollId();
            try {
                response = client.scroll(new SearchScrollRequest(scrollId), RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> resultMap = getResultMap(hit, highlightField);
                sourceList.add(resultMap);
            }
        }
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(response.getScrollId());
        try {
            client.clearScroll(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceList;
    }
    /**
     * 转换pojo对象
     * @param searchResponse
     * @param highlightField
     * @return
     */
    private  static List<SearchItem> setSearchResponse(SearchResponse searchResponse, String highlightField){
        List<SearchItem> list = new ArrayList<SearchItem>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Map<String, Object> resultMap = getResultMap(searchHit, highlightField);
            //String id, String title, String sell_point, long price, String image, String category_name
            list.add(new SearchItem((String) resultMap.get("id"),(String) resultMap.get("item_title"),(String) resultMap.get("item_sell_point")
                    , Long.valueOf((String) resultMap.get("item_price")),(String) resultMap.get("item_image"),(String) resultMap.get("item_category_name")));
        }
        return list;
    }
    /**
     * 获取高亮结果集
     *
     * @param: [hit, highlightField]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @auther: LHL
     */
    private static Map<String, Object> getResultMap(SearchHit hit, String highlightField) {
        hit.getSourceAsMap().put("id", hit.getId());
        if (StringUtils.isNotEmpty(highlightField)) {
            Text[] text = hit.getHighlightFields().get(highlightField).getFragments();
            String hightStr = null;
            if (text != null) {
                for (Text str : text) {
                    hightStr = str.string();
                }
                //遍历 高亮结果集，覆盖 正常结果集（意思是只要高亮的数据）
                hit.getSourceAsMap().put(highlightField, hightStr);
            }
        }
        return hit.getSourceAsMap();
    }
}
