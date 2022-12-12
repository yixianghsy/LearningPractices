package com.tulingxueyuan.mall.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.common.api.CommonPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.ProductAttributeCateDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttributeCategory;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {

    @Autowired
    PmsProductAttributeCategoryService attributeCategoryService;

    /**
     * 查询商品分类 列表
     * url:'/productAttribute/category/list',
     * method:'get',
     * params:params
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        Page page = attributeCategoryService.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

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

        boolean result = attributeCategoryService.add(productAttributeCategory);
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
    public CommonResult update(PmsProductAttributeCategory productAttributeCategory) {

        boolean result = attributeCategoryService.updateById(productAttributeCategory);
        if (result) {
            return CommonResult.success(result);
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
    public CommonResult delete(@PathVariable Long id) {

        boolean result = attributeCategoryService.removeById(id);
        if (result) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }

    }

    /**
     *  筛选属性下拉级联数据
     *  url:'/productAttribute/category/list/withAttr',
     *     method:'get'
     */
    @RequestMapping(value="/list/withAttr",method = RequestMethod.GET)
    public CommonResult getListWithAttr(){
         List<ProductAttributeCateDTO> list= attributeCategoryService.getListWithAttr();
         return CommonResult.success(list);
    }

}


