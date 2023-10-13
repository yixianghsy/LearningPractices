//package com.mall.sso.controller;
//import com.mall.api.CommonResult;
//import com.mall.sso.model.UmsResourceCategory;
//import com.mall.sso.service.UmsResourceCategoryService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 后台资源分类管理Controller
// * Created by macro on 2020/2/5.
// */
//@Controller
//@Api(tags = "UmsResourceCategoryController", description = "后台资源分类管理")
//@RequestMapping("/resourceCategory")
//public class UmsResourceCategoryController {
//    @Reference
//    private UmsResourceCategoryService resourceCategoryService;
//
//    @ApiOperation("查询所有后台资源分类")
//    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<UmsResourceCategory>> listAll() {
//        List<UmsResourceCategory> resourceList = resourceCategoryService.listAll();
//        return CommonResult.success(resourceList);
//    }
//
//    @ApiOperation("添加后台资源分类")
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult create(@RequestBody UmsResourceCategory umsResourceCategory) {
//        int count = resourceCategoryService.create(umsResourceCategory);
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }
//
//    @ApiOperation("修改后台资源分类")
//    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult update(@PathVariable Long id,
//                               @RequestBody UmsResourceCategory umsResourceCategory) {
//        int count = resourceCategoryService.update(id, umsResourceCategory);
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }
//
//    @ApiOperation("根据ID删除后台资源")
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult delete(@PathVariable Long id) {
//        int count = resourceCategoryService.delete(id);
//        if (count > 0) {
//            return CommonResult.success(count);
//        } else {
//            return CommonResult.failed();
//        }
//    }
//}
