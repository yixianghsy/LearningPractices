package com.mall.controller.controller;

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
@RequestMapping("/content/category")
public class ContentCatController {
    @Reference
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(
            @RequestParam(value="id", defaultValue="0") Long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }
    /**
     * 添加分类节点
     */
    @RequestMapping("/create")
    @ResponseBody
    public E3Result createCategory(Long parentId, String name) {
        return contentCategoryService.addContentCategory(parentId, name);
    }
}
