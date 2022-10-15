package com.e3mall;

import com.e3mall.pojo.EsPage;
import com.e3mall.pojo.SearchItem;
import com.e3mall.pojo.SearchResult;
import com.e3mall.search.Utit.EsUtil;
import com.e3mall.search.mapper.EsEntity;
import com.e3mall.search.mapper.ItemMapper;
import com.e3mall.search.mapper.ItemVO;
import com.e3mall.search.mapper.NBAPlayer;
import com.e3mall.search.message.ItemAddMessageListener;
import com.e3mall.search.service.SearchItemService;
import com.e3mall.search.service.impl.SearchServiceImpl;
import com.e3mall.utils.E3Result;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class E3SearchServiceApplicationTests {
	@Autowired
	private SearchItemService SearchItemServiceImpl;
	@Autowired
	private ItemAddMessageListener itemAddMessageListener;
	@Autowired
	private SearchServiceImpl searchService;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private EsUtil esUtil;

	private static final String INDEX = "nba_latest";
	@Test
	void  searchDataPage() throws Exception {
		SearchResult search = searchService.search(1, 5, "华为");
// 增强for循环遍历
		//TODO 如果是空的可以直接返回一个对象
		System.out.println("\n第二种遍历方式：增强for循环遍历 List 集合");
		for (Object item : search.getItemList()) {
			System.out.println(item);
		}
		System.out.println(search.getTotalPages());
		System.out.println(search.getRecordCount());


	}
	@Test
	void insertBatch() throws IOException {

		E3Result e3Result = SearchItemServiceImpl.importAllItems();
		System.out.println(e3Result.getMsg());
	}


	@Test
	void insertBatch2(){
		List<EsEntity> list = new ArrayList();
		int l =88;
		for (int i = 0; i < 10; i++) {
			NBAPlayer nbaPlayer = nbaPlayer();
			EsEntity esEntity = new EsEntity();
			esEntity.setId(String.valueOf(nbaPlayer.getId()+l));
			esEntity.setData(nbaPlayer);
			list.add(esEntity);
			l++;
		}

		esUtil.insertBatch(INDEX,list);
	}
	@Test
	void contextLoads() throws IOException {

		SearchItem searchItem = itemMapper.getItemById(536563);

		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("id",searchItem.getId())
				.field("item_title",searchItem.getTitle())
				.field("item_sell_point",searchItem.getSell_point())
				.field("item_price",searchItem.getPrice())
				.field("item_image",searchItem.getImage())
				.field("item_category_name",searchItem.getCategory_name())
				.endObject();
		//new IndexRequest(NBA_INDEX)----索引别名 .id 索引的key   .source---索引的值（文档_doc）
		esUtil.addData(builder,"item_title",searchItem.getId());

	}
	@Test
	void contextLoadsTest() {
		List<SearchItem> itemList = itemMapper.getItemList();
		            for(SearchItem searchItem: itemList){
						System.out.println(searchItem.getId());
            }
	}
	@Test
         	void  getItemById(){
		SearchItem itemById = itemMapper.getItemById(536563);
		System.out.println(itemById.getId());
	}

	@Test
	public void item(){
		List<SearchItem> itemList = itemMapper.getItemList();

		System.out.println(itemList.get(0).toString());
		SearchItem item1 = itemList.get(0);
		EsEntity<SearchItem> entity = new EsEntity<>(item1.getId().toString(), item1);
		esUtil.insertOrUpdateOne(EsUtil.INDEX_NAME, entity);
	}
	@Test
	public void getById() {
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(new TermQueryBuilder("id", 981821));
		List<SearchItem> res = esUtil.search(EsUtil.INDEX_NAME, builder, SearchItem.class);
		System.out.println(res.get(0));
	}
	public NBAPlayer nbaPlayer(){

		NBAPlayer nbaPlayer  = null;
		nbaPlayer = new NBAPlayer();
		nbaPlayer.setDisplayNameEn("test2");
		nbaPlayer.setId(20);
		nbaPlayer.setDisplayName("test");
		nbaPlayer.setAge(66565);

		return  nbaPlayer;
	}
}
