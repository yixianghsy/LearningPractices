package com.fanxb.esdemo.controller;

import com.fanxb.esdemo.entity.Book;
import com.fanxb.esdemo.entity.EsEntity;
import com.fanxb.esdemo.entity.SearchItem;
import com.fanxb.esdemo.service.SearchItemService;
import com.fanxb.esdemo.util.EsUtil;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/searchItem")
public class SearchitemController {
    @Autowired
    private SearchItemService searchItemService;

    @ResponseBody
    @GetMapping("/{id}")
    public SearchItem getById(@PathVariable("id") String id) {
        SearchItem byId = searchItemService.getById(id);
        return byId;
    }

    @PutMapping("/")
    public void putOne(@RequestBody SearchItem book) {
        searchItemService.putOne1(book);
    }

}
