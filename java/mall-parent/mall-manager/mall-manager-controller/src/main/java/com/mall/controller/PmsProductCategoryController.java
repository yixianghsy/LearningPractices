package com.mall.controller;


import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.mansger.PmsProductCategoryDTO;
import com.mall.mansger.dto.PmsProductCategoryWithChildrenItem;
import com.mall.mansger.dto.ProductCateChildrenDTO;
import com.mall.mansger.model.PmsProductCategory;
import com.mall.mansger.service.PmsProductCategoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类管理Controller
 * Created by macro on 2018/4/26.
 */
@RestController
//@Api(tags = "PmsProductCategoryController")
//@Tag(name = "PmsProductCategoryController", description = "商品分类管理")
@RequestMapping("/productCategory")
public class PmsProductCategoryController {
    @Reference
    private PmsProductCategoryService productCategoryService;



//    @ApiOperation("查询所有一级分类及子分类")
    @RequestMapping(value = "/list/withChildren", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductCategoryWithChildrenItem>> listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
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

        return CommonResult.success(productCategoryService.list(parentId,pageNum,pageSize));
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
        //方法名用这removeById 用这个更合适吧
        boolean result = productCategoryService.delete(id);
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

        PmsProductCategory productCategory = productCategoryService.getItem(id);
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
