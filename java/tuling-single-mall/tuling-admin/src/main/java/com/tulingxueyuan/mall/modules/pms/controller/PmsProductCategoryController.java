package com.tulingxueyuan.mall.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.common.api.CommonPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.PmsProductCategoryDTO;
import com.tulingxueyuan.mall.dto.ProductCateChildrenDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProductCategory;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 产品分类 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/productCategory")
public class PmsProductCategoryController {

    @Autowired
    PmsProductCategoryService productCategoryService;

    /**
     *  url:'/productCategory/list/'+parentId,
     *     method:'get',
     *     params:     {
     *           pageNum: 1,
     *           pageSize: 5
     *         },
     */
    @RequestMapping(value="/list/{parentId}",method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProductCategory>> getList(@PathVariable Long parentId,
                                                                @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
                                                                @RequestParam(value="pageSize",defaultValue = "5") Integer pageSize){

        Page page= productCategoryService.list(parentId,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }



    /**
     *  修改导航栏显示状态
     *  url: /productCategory/update/navStatus
     *  method:'post',
     *  data:data
     *      data.append('ids',ids);
     *      data.append('navStatus',row.navStatus);
     */
    @RequestMapping(value="/update/navStatus",method = RequestMethod.POST)
    public CommonResult updateNavStatus(@RequestParam(value="ids") List<Long> ids,
                                        @RequestParam(value="navStatus") Integer navStatus){

        boolean result = productCategoryService.updateNavStatus(ids, navStatus);
        if(result){
            return CommonResult.success(result);
        }
        else {
            return CommonResult.failed();
        }

    }

    /**
     * 商品分类删除
     *  url:'/productCategory/delete/'+id,
     *     method:'post'
     */
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long id){

        boolean result = productCategoryService.removeById(id);
        if(result){
            return CommonResult.success(result);
        }
        else {
            return CommonResult.failed();
        }

    }

    /**
     * 分类添加
     * url:'/productCategory/create',
     *     method:'post',
     *     data:data
     */
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public CommonResult create(@RequestBody PmsProductCategoryDTO productCategoryDTO){

        boolean result = productCategoryService.CustomSave(productCategoryDTO);
        if(result){
            return CommonResult.success(result);
        }
        else {
            return CommonResult.failed();
        }

    }


    /**
     *  根据id获取商品分类
     *  return request({
     *     url:'/productCategory/'+id,
     *     method:'get',
     *   })
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public CommonResult<PmsProductCategory> getById(@PathVariable Long id){

        PmsProductCategory productCategory = productCategoryService.getById(id);
        return CommonResult.success(productCategory);
    }

    /**
     * 修改
     *   url:'/productCategory/update/'+id,
     *     method:'post',
     *     data:data
     *
     */
    @RequestMapping(value="/update/{id}",method = RequestMethod.POST)
    public CommonResult update(
            @RequestBody PmsProductCategoryDTO productCategoryDTO){

        boolean result = productCategoryService.update(productCategoryDTO);
        if(result){
            return CommonResult.success(result);
        }
        else {
            return CommonResult.failed();
        }

    }

    /**
     *   获取商品一级分类和二级分类的下拉级联数据
     *   url:'/productCategory/list/withChildren',
     *     method:'get'
     */
    @RequestMapping("/list/withChildren")
    public  CommonResult getWithChildren(){
        List<ProductCateChildrenDTO> list= productCategoryService.getWithChildren();

        return CommonResult.success(list);
    }

}

