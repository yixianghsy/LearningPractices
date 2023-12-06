package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.content.model.CmsSubject;;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.model.PmsProductCategory;
import com.mall.portal.domain.HomeContentResult;
import com.mall.portal.dto.HomeGoodsSaleDTO;
import com.mall.portal.dto.HomeMenusBannerDTO;
import com.mall.portal.dto.HomeMenusDTO;
import com.mall.mansger.model.SmsHomeAdvertise;
import com.mall.portal.service.HomeService;
import com.mall.portal.service.PmsProductCategoryService;
import com.mall.portal.service.SmsHomeAdvertiseService;
import com.mall.portal.service.SmsHomeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页内容管理Controller
 * Created on 2019/1/28.
 */
@Controller
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/home")
public class HomeController {

    @Autowired
    PmsProductCategoryService productCategoryService;

    @Autowired
    SmsHomeCategoryService homeCategoryService;

    @Autowired
    SmsHomeAdvertiseService homeAdvertiseService;

    @Autowired
    private HomeService homeService;
    @ApiOperation("首页内容信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }

    @ApiOperation("分页获取推荐商品")
    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }
    @ApiOperation("获取首页商品分类")
    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }

    @ApiOperation("根据分类获取专题")
    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
        return CommonResult.success(subjectList);
    }
    @ApiOperation("分页获取人气推荐商品")
    @RequestMapping(value = "/hotProductList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.hotProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }
    @ApiOperation("分页获取新品推荐商品")
    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.newProductList(pageNum,pageSize);
        return CommonResult.success(productList);
    }
    @ApiOperation("获取首页类型导航栏和数据")
    @RequestMapping(value = "/menus_banner", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getMenus() {
        // 分类导航
        List<HomeMenusDTO> list= productCategoryService.getMenus();

        // banner
        List<SmsHomeAdvertise> homeAdvertisesList= homeAdvertiseService.getHomeBanners();

        HomeMenusBannerDTO homeMenusBannerDTO=new HomeMenusBannerDTO();
        homeMenusBannerDTO.setHomeMenusList(list);
        homeMenusBannerDTO.setHomeAdvertisesList(homeAdvertisesList);


        return CommonResult.success(homeMenusBannerDTO);
    }
    @ApiOperation("goods_sale")
    @RequestMapping(value = "/goods_sale", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getGoodsSale() {
        List<HomeGoodsSaleDTO> list= homeCategoryService.getGoodsSale();
        return CommonResult.success(list);
    }
}
