package com.xdclass.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xdclass.search.dao.NBAPlayerDao;
import com.xdclass.search.model.NBAPlayer;
import com.xdclass.search.service.NBAPlayerService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NBAPlayerSerivceImpl implements NBAPlayerService {

    @Resource
    private RestHighLevelClient client;

    @Resource
    private NBAPlayerDao nbaPlayerDao;

    private static final String NBA_INDEX = "nba_latest";

    private static final int START_OFFSET = 0;

    private static final  int MAX_COUNT = 1000;

    @Override
    public boolean addPlayer(NBAPlayer player, String id) throws IOException {
        IndexRequest request = new IndexRequest(NBA_INDEX).id(id).source(beanToMap(player));
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return false;
    }

    @Override
    public Map<String,Object> getPlayer(String id) throws IOException {
        GetRequest getRequest = new GetRequest(NBA_INDEX,id);
        GetResponse response = client.get(getRequest,RequestOptions.DEFAULT);
        return response.getSource();
    }

    @Override
    public boolean updatePlayer(NBAPlayer player,String id) throws IOException {
        UpdateRequest request = new UpdateRequest(NBA_INDEX,id).doc(beanToMap(player));
        UpdateResponse response = client.update(request,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return true;
    }

    @Override
    public boolean deletePlayer(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(NBA_INDEX,id);
        client.delete(request,RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public boolean deleteAllPlayer() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(NBA_INDEX);
        BulkByScrollResponse response = client.deleteByQuery(request,RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public boolean importAll() throws IOException {
        List<NBAPlayer> list = nbaPlayerDao.selectAll();
        for(NBAPlayer player: list){
            addPlayer(player,String.valueOf(player.getId()));
        }
        return true;
    }

    @Override
    public List<NBAPlayer> searchMatch(String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }

    @Override
    public List<NBAPlayer> searchTerm(String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }


    @Override
    public List<NBAPlayer> searchMatchPrefix(String key,String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(NBA_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.prefixQuery(key,value));
        searchSourceBuilder.from(START_OFFSET);
        searchSourceBuilder.size(MAX_COUNT);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));

        SearchHit[] hits = response.getHits().getHits();
        List<NBAPlayer> playerList = new LinkedList<>();
        for(SearchHit hit: hits){
            NBAPlayer player = JSONObject.parseObject(hit.getSourceAsString(),NBAPlayer.class);
            playerList.add(player);
        }

        return playerList;
    }





    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if(beanMap.get(key) != null)
                    map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
