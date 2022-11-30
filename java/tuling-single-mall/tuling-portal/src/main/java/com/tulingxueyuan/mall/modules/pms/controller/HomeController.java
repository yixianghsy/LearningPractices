package com.tulingxueyuan.mall.modules.pms.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.HomeMenusBannerDTO;
import com.tulingxueyuan.mall.dto.HomeMenusDTO;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductCategoryService;
import com.tulingxueyuan.mall.modules.sms.model.SmsHomeAdvertise;
import com.tulingxueyuan.mall.modules.sms.service.SmsHomeAdvertiseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 *
 * 首页控制器
 */
@RestController
@Api(tags = "HomeController",description = "首页内容管理")
@RequestMapping("/home")
public class HomeController {
    @Autowired
    PmsProductCategoryService productCategoryService;

    @Autowired
    SmsHomeAdvertiseService homeAdvertiseService;
    /**
     * 获取首页类型导航栏和数据
     * get("/home/menus")
     */
    @RequestMapping(value = "/menus_banner",method = RequestMethod.GET)
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
}
