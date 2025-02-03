package com.mall.portal.controller;
import com.mall.api.CommonResult;
import com.mall.mansger.dto.HomeMenusDTO;
import com.mall.mansger.service.PmsProductCategoryService;
import com.mall.marketing.dto.HomeGoodsSaleDTO;
import com.mall.marketing.service.SmsHomeAdvertiseService;
import com.mall.marketing.service.SmsHomeCategoryService;
import com.mall.portal.dto.HomeMenusBannerDTO;
import com.mall.marketing.model.SmsHomeAdvertise;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页内容管理Controller
 * Created on 2019/1/28.
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Reference
    PmsProductCategoryService productCategoryService;

    @Reference
    SmsHomeCategoryService homeCategoryService;
    @Reference
    SmsHomeAdvertiseService homeAdvertiseService;

//    @Autowired
//    private HomeService homeService;

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

    //    @ApiOperation("goods_sale")
    @RequestMapping(value = "/goods_sale", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getGoodsSale() {
        List<HomeGoodsSaleDTO> list= homeCategoryService.getGoodsSale();
        return CommonResult.success(list);
    }

//    // TODO 内容管理
//    @RequestMapping(value = "/content", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<HomeContentResult> content() {
//        HomeContentResult contentResult = homeService.content();
//        return CommonResult.success(contentResult);
//    }
//    // TODO 后台管理
//    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
//                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
//        return CommonResult.success(productList);
//    }
//    // TODO 后台管理
//
//    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
//        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
//        return CommonResult.success(productCategoryList);
//    }
//    // TODO 内容管理
//    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
//                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
//                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<CmsSubject> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
//        return CommonResult.success(subjectList);
//    }
//    // TODO 后台管理
//
//    @RequestMapping(value = "/hotProductList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
//        List<PmsProduct> productList = homeService.hotProductList(pageNum,pageSize);
//        return CommonResult.success(productList);
//    }
//    // TODO 后台管理
//    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
//        List<PmsProduct> productList = homeService.newProductList(pageNum,pageSize);
//        return CommonResult.success(productList);
//    }


}
