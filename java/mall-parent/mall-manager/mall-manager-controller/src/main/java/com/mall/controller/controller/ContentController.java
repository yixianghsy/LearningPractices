//package com.mall.controller.controller;
//import com.mall.content.service.ContentService;
//import com.mall.modules.content.TbContent;
//import com.mall.pojo.EasyUIDataGridResult;
//import com.mall.utils.E3Result;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * 内容管理Controller
// * <p>Title: ContentController</p>
// * <p>Description: </p>
// * <p>Company: www.itcast.cn</p>
// * @version 1.0
// */
//@Controller
//@RequestMapping("/content")
//public class ContentController {
//    @Reference
//    private ContentService contentService;
//
//    @ResponseBody
//    @RequestMapping("/query/list")
//    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, Integer page, Integer rows) {
//        return contentService.getContentListByCategoryId(categoryId, page, rows);
//    }
//    @RequestMapping("/save")
//    @ResponseBody
//    public E3Result addContent(TbContent content) {
//        return contentService.addContent(content);
//    }
//}
