package com.tulingxueyuan.mall.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.common.api.CommonPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.RelationAttrInfoDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductAttribute;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Autowired
    PmsProductAttributeService productAttributeService;

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

        Page page = productAttributeService.list(cid, type, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
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

        boolean result = productAttributeService.updateById(productAttribute);
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

//        boolean result = productAttributeService.delete(ids);
        boolean result = productAttributeService.removeByIds(ids);
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

