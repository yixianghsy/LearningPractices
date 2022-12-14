package com.mall.search.Utit;

import com.alibaba.fastjson.JSON;
import com.mall.pojo.SearchItem;
import com.mall.pojo.SearchResult;
import com.mall.search.mapper.EsEntity;
import org.apache.commons.lang3.StringUtils;
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
 * ??????????????????
 * ??????????????????
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
     * ????????????
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
     * ??????????????????
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
            //??????mapping
            request.mapping(CREATE_INDEX, XContentType.JSON);
            CreateIndexResponse res = client.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("???????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Description: ????????????index????????????
     *
     * @param index index???
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
     * Description: ??????/??????????????????
     *
     * @param index  index
     * @param entity ??????
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
     * ????????????
     *
     * @param content   ??????????????????
     * @param indexName ????????????????????????
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
     * ??????????????????
     *
     * @param list  ????????????????????????
     * @param index ????????????????????????
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
     * Description: ????????????
     *
     * @param index  index
     * @param idList ???????????????
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
     * Description: ??????
     *
     * @param index   index
     * @param builder ????????????
     * @param c       ???????????????
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
     * Description: ??????index
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
        //????????????????????????,?????????10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ??????????????????  ?????? ?????? ,?????????
     *
     * ??????????????????????????????????????????????????????startPage,paageSize,query)
     * index ??????
     *  highlightField ??????????????????
     *   sortField     ??????
     *   fields        ??????????????????
     *
     * @param startPage      ?????????
     * @param pageSize       ??????????????????
     * @param query          ????????????
     * @return ??????
     */
    public  SearchResult searchDataPage(int startPage, int pageSize, String query) {
        TermQueryBuilder builder = QueryBuilders.termQuery(HIGHLIGH_FIELD, query);
        //??????
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //?????????????????????????????????????????????????????????
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        if (StringUtils.isNotEmpty(HIGHLIGH_FIELD)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //????????????
            highlightBuilder.preTags("<em style=\"color:red\">");
            //????????????
            highlightBuilder.postTags("</em>");
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(HIGHLIGH_FIELD);
            //???????????????
            highlightTitle.highlighterType("unified");
            // ??????????????????
            highlightBuilder.field(highlightTitle);
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // ????????????????????????????????????
        searchSourceBuilder.explain(true);
        if (startPage <= 0) {
            startPage = 0;
        }
        //?????? pageSize???10 ??????startPage>9990 (10000-pagesize) ?????? 20  ?????? >9980 ?????? 50 ??????>9950
        //????????????  TODO
        if (startPage > (10000 - pageSize)) {
            searchSourceBuilder.query(builder);
            searchSourceBuilder
                    // .setScroll(TimeValue.timeValueMinutes(1))
                    .size(10000);
            //??????????????? ????????? Elasticsearch head ??? Kibana  ???????????????
            LOGGER.info("\n{}", searchSourceBuilder);
            // ????????????,????????????????????????
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //?????????????????????
            long totalHits = searchResponse.getHits().getTotalHits().value;
            if (searchResponse.status().getStatus() == 8200) {
                //??????scrollId????????????
                List<Map<String, Object>> result = disposeScrollResult(searchResponse, HIGHLIGH_FIELD);
                List<Map<String, Object>> sourceList = result.stream().parallel().skip((startPage - 1 - (10000 / pageSize)) * pageSize).limit(pageSize).collect(Collectors.toList());
                return new SearchResult(startPage, pageSize, (int) totalHits, sourceList);
            }
        } else {//????????????
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.query(builder);
            /*MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("username", "pretty");
		matchQueryBuilder.fuzziness(Fuzziness.AUTO);//????????????????????????????????????
		matchQueryBuilder.prefixLength(3);//??????????????????????????????????????????
		matchQueryBuilder.maxExpansions(10);//??????????????????????????????????????????????????????
		searchSourceBuilder.query(matchQueryBuilder);*/
            // ????????????
            searchSourceBuilder
                    //??????from??????????????????????????????????????????????????????0
                    // .from(startPage)
                    .from((startPage - 1) * pageSize)
                    //??????size??????????????????????????????????????????????????????10
                    .size(pageSize);
            //??????????????? ????????? Elasticsearch head ??? Kibana  ???????????????
            LOGGER.info("\n{}", searchSourceBuilder);
            // ????????????,????????????????????????
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //????????????????????????????????????
            long totalHits = searchResponse.getHits().getTotalHits().value;
            //c??????????????????????????????
            long length = searchResponse.getHits().getHits().length;
            LOGGER.debug("????????????[{}]?????????,??????????????????[{}]", totalHits, length);
            //??????????????????????????????200???
            if (searchResponse.status().getStatus() == 200) {
                // ????????????
                List<SearchItem>  sourceList = setSearchResponse(searchResponse, HIGHLIGH_FIELD);
                //??????????????????????????????????????????totalHits, sourceList
                return new SearchResult(startPage, pageSize, (int) totalHits, sourceList);
            }
        }
        //????????????null
        return null;
    }
    /**
     * ??????scroll??????
     *
     * @param: [response, highlightField]
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @auther: LHL
     */
    private static List<Map<String, Object>> disposeScrollResult(SearchResponse response, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<>();
        //??????scrollId????????????
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
     * ??????pojo??????
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
     * ?????????????????????
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
                //?????? ???????????????????????? ???????????????????????????????????????????????????
                hit.getSourceAsMap().put(highlightField, hightStr);
            }
        }
        return hit.getSourceAsMap();
    }
}
