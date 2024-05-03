package com.mall.controller;
import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.mansger.dto.ProductAttributeCateDTO;
import com.mall.mansger.model.PmsProductAttributeCategory;
import com.mall.mansger.service.PmsProductAttributeCategoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品属性分类管理Controller
 * Created by macro on 2018/4/26.
 */
@RestController
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {
    @Reference
    private PmsProductAttributeCategoryService productAttributeCategoryService;
    /**
     * 添加商品类型
     * return request({
     * url:'/productAttribute/category/create',
     * method:'post',
     * data:data
     * })
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(PmsProductAttributeCategory productAttributeCategory) {

        boolean result = productAttributeCategoryService.add(productAttributeCategory);
        if (result) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 商品类型修改
     * return request({
     * url:'/productAttribute/category/update/'+id,
     * method:'post',
     * data:data
     * })
     */

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(PmsProductAttributeCategory productAttributeCategory) {
        int count = productAttributeCategoryService.update(productAttributeCategory.getId(), productAttributeCategory.getName());
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
    /**
     * 商品类型删除
     * url:'/productAttribute/category/delete/'+id,
     * method:'get'
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = productAttributeCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductAttributeCategory> getItem(@PathVariable Long id) {
        PmsProductAttributeCategory productAttributeCategory = productAttributeCategoryService.getItem(id);
        return CommonResult.success(productAttributeCategory);
    }


    /**
     * 查询商品分类 列表
     * url:'/productAttribute/category/list',
     * method:'get',
     * params:params
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        CommonPage list = productAttributeCategoryService.list(pageNum, pageSize);
        return CommonResult.success(list);
    }



    /**
     *  筛选属性下拉级联数据
     *  url:'/productAttribute/category/list/withAttr',
     *     method:'get'
     */
    @RequestMapping(value="/list/withAttr",method = RequestMethod.GET)
    public CommonResult getListWithAttr(){
        List<ProductAttributeCateDTO> list= productAttributeCategoryService.getListWithAttr();
        return CommonResult.success(list);
    }

}
