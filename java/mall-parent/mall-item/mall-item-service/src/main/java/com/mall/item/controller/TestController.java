package com.mall.item.controller;

import com.mall.item.service.ItemService;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 文件描述
 **/
@Controller
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Resource
    private ItemService itemService;
    /**
     * item-lits页面商品显示
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(@RequestParam(defaultValue="1")Integer page , @RequestParam(defaultValue="3")Integer rows){

        //调用服务查询商品列表
        EasyUIDataGridResult easyUIDataGridResult = itemService.getItemListgetItemList(page,rows);
        String json = JsonUtils.objectToJson(easyUIDataGridResult);
        System.out.println("ItemController.getItemList"+json);
        return easyUIDataGridResult;
    }
    @GetMapping("/test")
    @ResponseBody
    public   String  getoder(){
        itemService.testOrderId("325");
return   "么有报错";
    }
}
