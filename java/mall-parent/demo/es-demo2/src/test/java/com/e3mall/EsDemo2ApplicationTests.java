package com.e3mall;

import com.alibaba.fastjson.JSONException;

import com.e3mall.search.Utit.ElasticsearchUtil;
import com.e3mall.search.model.NBAPlayer;
import com.e3mall.search.pojo.EsEntity;
import com.e3mall.search.pojo.EsPage;
import com.e3mall.search.service.EsService;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanMap;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@SpringBootTest
class EsDemo2ApplicationTests {
	private static final String INDEX = "nba_latest";
	@Autowired
	private EsService esService;
@Autowired
private ElasticsearchUtil elasticsearchUtil;

	@Resource
	private RestHighLevelClient client;
	@Test
	void contextLoads() throws IOException {

		XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
					.field("id","test")
					.field("item_title","阿尔卡特 (OT-979) 冰川白 联通3G手机")
					.field("item_sell_point","清仓！仅上海仓有货！")
					.field("item_price","1100")
					.field("item_image","http://image.e3mall.cn/jd/4ef8861cf6854de9889f3db9b24dc371.jpg")
					.field("item_category_name","平板电视")
					.endObject();
		//new IndexRequest(NBA_INDEX)----索引别名 .id 索引的key   .source---索引的值（文档_doc）
		esService.addData(builder,INDEX,"6699");

	}
	/**
	 * 转map
	 * @param bean
	 * @param <T>
	 * @return
	 */

	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<>();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				if(beanMap.get(key) != null) {
					map.put(key + "", beanMap.get(key));
				}
			}
		}
		return map;
	}
	@Test
	void   elasticsearchUtiTest() {
		boolean elasticsearchUtil = esService.createIndex("test2");
		System.out.println(elasticsearchUtil);
	}

	@Test
	public void indexTest() {
		try {
			// 借助indexRequest的json拼接工具
			IndexRequest indexRequest = new IndexRequest();
			XContentBuilder builder = JsonXContent.contentBuilder()
					.startObject()
					.startObject("properties")
					.startObject("title")
					.field("type","text")
					.field("analyzer","ik_max_word")
					.endObject()
					.startObject("content")
					.field("type","text")
					.field("index","analyzed")
					.field("analyzer","ik_max_word")
					.endObject()
					.startObject("uniqueId")
					.field("type","keyword")
					.field("index","not_analyzed")
					.endObject()
					.startObject("created")
					.field("type","date")
					.field("format","strict_date_optional_time||epoch_millis")
					.endObject()
					.endObject()
					.endObject();
			indexRequest.source(builder);
			// 生成json字符串
			String source = indexRequest.source().utf8ToString();
			System.out.println(source);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Test
	void insertBatch(){
		List<EsEntity> list = new ArrayList();
		int l = 1;
		for (int i = 0; i < 10; i++) {
			NBAPlayer nbaPlayer = nbaPlayer();
			EsEntity esEntity = new EsEntity();
			esEntity.setId(String.valueOf(nbaPlayer.getId()+l));
			esEntity.setData(nbaPlayer);
			list.add(esEntity);
			l++;
		}

		esService.insertBatch(INDEX,list);
	}
	@Test
	 void  tes2(){
		EsPage esPage = esService.searchDataPage(1, 5,  "james");
		List recordList = esPage.getRecordList();


		System.out.println("+++++++++++/n"+recordList.get(0).toString());


	}

	//把List<Map<String, Object>>的字符串转换成JsonArray
	public static String parseListForMapsToJsonArrayStr(List<Map<String, Object>> list) {
		String jsonArrayStr = null;
		if(list != null && list.size() != 0) {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;
			Object value = null;
			for(Map<String, Object> map : list) {
				jsonObject = new JSONObject();
				Set<String> set = map.keySet();
				for(String key : set) {
					value = map.get(key);
					if(value != null) {
						try {
							jsonObject.put(key, value.toString());
						} catch (JSONException | org.json.JSONException e) {
							e.printStackTrace();
						}
					}
				}
				if(jsonObject.length() != 0) {
					jsonArray.put(jsonObject);
				}
			}
			jsonArrayStr = jsonArray.toString();
		}

		return jsonArrayStr;
	}

	public NBAPlayer nbaPlayer(){

		NBAPlayer nbaPlayer  = null;
			nbaPlayer = new NBAPlayer();
			nbaPlayer.setDisplayNameEn("test2");
			nbaPlayer.setId(30);
			nbaPlayer.setDisplayName("test");
			nbaPlayer.setAge(66565);

		return  nbaPlayer;
	}
	}
