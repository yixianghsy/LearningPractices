package com.fanxb;
import com.fanxb.esdemo.entity.Book;
import com.fanxb.esdemo.entity.SearchItem;
import com.fanxb.esdemo.service.BookService;
import com.fanxb.esdemo.service.SearchItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {
	@Autowired
	private SearchItemService searchItemService;


	@Autowired
	private BookService bookService;


	@Test
	public void addOneTest1() {

		searchItemService.putOne1(new SearchItem("222","测试","经典回顾！超值价格值得拥有。",169900,
				"http","手机"));

	}

	@Test
	public void searchByUserIdAndName(){
		System.out.println(searchItemService.searchByUserIdAndName("00011","http").toString());
	}

	@Test
	public void searchByUserIdAndName2(){
		System.out.println(bookService.searchByUserIdAndName(1,"美人鱼").toString());
	}

		@Test
	public void getOne1() {
		System.out.println(searchItemService.getById("222").toString());
	}
	@Test
	public void getOne() {
		System.out.println(bookService.getById(00011).toString());
	}

	@Test
	public void getAll() {
		List<Book> res = bookService.getAll();
		res.forEach(System.out::println);
	}

	@Test
	public void addOneTest() {
		bookService.putOne(new Book(1, 1, "格林童话"));
		bookService.putOne(new Book(2, 1, "美人鱼"));
	}

	@Test
	public void addBatchTest() {
		List<Book> list = new ArrayList<>();
		list.add(new Book(3, 1, "第一本书"));
		list.add(new Book(4, 1, "第二本书"));
		bookService.putList(list);
	}

	@Test
	public void deleteBatch() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(3);
		bookService.deleteBatch(list);
	}

	@Test
	public void deleteByQuery(){
		bookService.deleteByUserId(1);
	}

}
