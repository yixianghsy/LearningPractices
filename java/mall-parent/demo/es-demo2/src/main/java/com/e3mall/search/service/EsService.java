package com.e3mall.search.service;

import com.alibaba.fastjson.JSON;
import com.e3mall.search.Utit.ElasticsearchUtil;
import com.e3mall.search.model.NBAPlayer;
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
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Service
public class EsService {

    private static final String INDEX = "nba_latest";

    private static final Logger LOGGER = LoggerFactory.getLogger(EsService.class);

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
     * 数据添加
     *
     * @param content   要增加的数据
     * @param indexName 索引，类似数据库
     * @param id        id
     * @return String
     * @auther: LHL
     */
    public static String addData(XContentBuilder content, String indexName, String id) {
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
     * 根据条件删除
     *
     * @param builder   要删除的数据  new TermQueryBuilder("userId", userId)
     * @param indexName 索引，类似数据库
     * @return
     * @auther: LHL
     */
    public void deleteByQuery(String indexName, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
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
     * 批量删除
     *
     * @param idList 要删除的数据id
     * @param index  索引，类似数据库
     * @return
     * @auther: LHL
     */
    public static <T> void deleteBatch(String index, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
        try {
            client.bulk(request, RequestOptions.DEFAULT);
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
    public static EsPage searchDataPage( int startPage, int pageSize, String query) {
        String highlightField ="displayNameEn";
        TermQueryBuilder builder = QueryBuilders.termQuery("displayNameEn", query);

        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置一个可选的超时，控制允许搜索的时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //设置前缀
            highlightBuilder.preTags("<span style='color:red' >");
            //设置后缀
            highlightBuilder.postTags("</span>");
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(highlightField);
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
                List<Map<String, Object>> result = disposeScrollResult(searchResponse, highlightField);
                List<Map<String, Object>> sourceList = result.stream().parallel().skip((startPage - 1 - (10000 / pageSize)) * pageSize).limit(pageSize).collect(Collectors.toList());
                return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
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
                List<NBAPlayer>  sourceList = setSearchResponse(searchResponse, highlightField);
                //可以看看只要自己需要的，比如totalHits, sourceList
                return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
            }
        }
        //否则返回null
        return null;
    }

    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse 搜索的结果集
     * @param highlightField 高亮字段
     */
//    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
//        List<Map<String, Object>> sourceList = new ArrayList<>();
//        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
//            Map<String, Object> resultMap = getResultMap(searchHit, highlightField);
//            //TDOO 此处可以根据map的Key转为对象添加到list中
//            System.out.println(".............."+resultMap.get("displayNameEn"));
//            sourceList.add(resultMap);
//        }
//        return sourceList;
//    }

    /**
     * 转换pojo对象
     * @param searchResponse
     * @param highlightField
     * @return
     */
    private  static List<NBAPlayer> setSearchResponse(SearchResponse searchResponse, String highlightField){
        List<NBAPlayer> list = new ArrayList<NBAPlayer>();
        NBAPlayer nbaPlayer = new NBAPlayer();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Map<String, Object> resultMap = getResultMap(searchHit, highlightField);
            //TDOO 此处可以根据map的Key转为对象添加到list中
            nbaPlayer.setId(Integer.valueOf((String) resultMap.get("id")));
            nbaPlayer.setDisplayName((String) resultMap.get("displayName"));
            nbaPlayer.setDisplayNameEn((String) resultMap.get("displayNameEn"));
            nbaPlayer.setCountry((String) resultMap.get("country"));
            nbaPlayer.setCountryEn((String) resultMap.get("countryEn"));
            nbaPlayer.setPlayerId((String) resultMap.get("playerId"));
            list.add(nbaPlayer);
            //为节约时间，以下set省略
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
        System.out.println("getResultMap:"+hit.getSourceAsMap().put("id", hit.getId()));
        if (StringUtils.isNotEmpty(highlightField)) {
            Text[] text = hit.getHighlightFields().get(highlightField).getFragments();
            String hightStr = null;
            if (text != null) {
                for (Text str : text) {
                    hightStr = str.string();
                }
                System.out.println("hightStr:"+hightStr);
                //遍历 高亮结果集，覆盖 正常结果集（意思是只要高亮的数据）
                hit.getSourceAsMap().put(highlightField, hightStr);
//                System.out.println("hit.getSourceAsMap().put(highlightField, hightStr)"+hit.getSourceAsMap().put(highlightField, hightStr));
                System.out.println(" hit.getSourceAsMap():"+ hit.getSourceAsMap());
            }
        }
        return hit.getSourceAsMap();
    }


    public static <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
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
            // 借助indexRequest的json拼接工具
            IndexRequest indexRequest = new IndexRequest();
            XContentBuilder builder = JsonXContent.contentBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("title")
                    .field("type", "text")
                    .field("analyzer", "ik_max_word")
                    .endObject()
                    .startObject("content")
                    .field("type", "text")
                    .field("index", "true")
                    .field("analyzer", "ik_max_word")
                    .endObject()
                    .startObject("uniqueId")
                    .field("type", "keyword")
                    .field("index", "true")
                    .endObject()
                    .startObject("created")
                    .field("type", "date")
                    .field("format", "strict_date_optional_time||epoch_millis")
                    .endObject()
                    .endObject()
                    .endObject();
            indexRequest.source(builder);
            // 生成json字符串
            String source = indexRequest.source().utf8ToString();
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
            request.mapping(source, XContentType.JSON);
            CreateIndexResponse res = client.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            } else {
                return res.isAcknowledged();
            }
            //
//            CreateIndexRequest request = new CreateIndexRequest(indexName).source(builder);
//            //设置创建索引超时2分钟
//            request.setTimeout(TimeValue.timeValueMinutes(2));
//            createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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
}