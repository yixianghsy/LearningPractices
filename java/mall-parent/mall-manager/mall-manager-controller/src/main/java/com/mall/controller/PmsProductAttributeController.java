package com.mall.controller;

import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.mansger.dto.PmsProductAttributeParam;
import com.mall.mansger.dto.ProductAttrInfo;
import com.mall.mansger.dto.RelationAttrInfoDTO;
import com.mall.mansger.model.PmsProductAttribute;
import com.mall.mansger.service.PmsProductAttributeService;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品属性管理Controller
 * Created by macro on 2018/4/26.
 */
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {
    @Reference
    private PmsProductAttributeService productAttributeService;
    /**
     * 商品分类—商品属性数据列表
     * url:'/productAttribute/list/'+cid,
     * method:'get',
     * params:params
     */
    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public CommonResult<CommonPage> getList(@PathVariable Long cid,
                                            @RequestParam(value = "type") Integer type,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return CommonResult.success(productAttributeService.list(cid, type, pageNum, pageSize));
    }


    /**
     * 属性添加
     * return request({
     * url:'/productAttribute/create',
     * method:'post',
     * data:data
     * })
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult create(@RequestBody PmsProductAttribute productAttribute) {

        boolean result = productAttributeService.create(productAttribute);
        if (result) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }

    }


    /**
     * 属性修改
     * url:'/productAttribute/update/'+id,
     * method:'post',
     * data:data
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult update(@RequestBody PmsProductAttribute productAttribute) {
        Boolean result = productAttributeService.update(productAttribute);
        if (result) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 根据id获取商品属性
     * url:'/productAttribute/'+id,
     * method:'get'
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<PmsProductAttribute> getById(@PathVariable Long id) {

        PmsProductAttribute productCategory = productAttributeService.getById(id);
        return CommonResult.success(productCategory);
    }


    /**
     * url:'/productAttribute/delete',
     * method:'post',
     * data:data
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {

        boolean result = productAttributeService.delete(ids);
        if (result) {
            return CommonResult.success(result);
        } else {
            return CommonResult.failed();
        }
    }
    /**
     *  根据商品分类id获取关联的筛选属性
     *   url:'/productAttribute/attrInfo/'+productCategoryId,
     *     method:'get'
     */
    @RequestMapping(value="/attrInfo/{cId}")
    public CommonResult getRelationAttrInfoByCid(@PathVariable Long cId){
        List<RelationAttrInfoDTO> list=  productAttributeService.getRelationAttrInfoByCid(cId);
        return CommonResult.success(list);
    }

}
