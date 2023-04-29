package com.mall.mapper.controller;

import com.mall.content.service.ContentCategoryService;
import com.mall.pojo.EasyUITreeNode;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 内容分类管理Controller
 * <p>Title: ContentCatController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class ContentCatController {
    @Reference
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id", defaultValue="0")Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
        return  list;
    }
    /**
     * 添加分类节点
     */
    @ResponseBody
    @RequestMapping(value="/content/category/create", method= RequestMethod.POST)
    public E3Result createContentCategory(Long parentId, String name){
        //调用服务添加节点
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }
}
