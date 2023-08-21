package com.mall.search.controller;

import com.mall.pojo.SearchResult;
import com.mall.search.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品搜索Controller
 * <p>Title: SearchController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class SearchController {

	@Reference
	private SearchService searchService;

	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;

	@RequestMapping("/search.html")
	public String searchItemList(String keyword, @RequestParam(defaultValue="1") Integer page, Model model) throws Exception {
		//本地环境是GBK需要转,部署服务器就要注释掉
//		keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
		//查询商品列表
		SearchResult searchResult = searchService.search(page,PAGE_ROWS,keyword);
		//把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());
		//异常测试
		//int a = 1/0;
		//返回逻辑视图
		return "search";
	}
}
