package com.tulingxueyuan.mall.modules.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.common.api.CommonPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.modules.pms.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    PmsBrandService brandService;
    /**
     *  品牌数据列表
     *      在商品中进行共用
     *  url:'/brand/list',
     *     method:'get',
     *     params:params
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list(
            @RequestParam(value="keyword",defaultValue = "") String keyword,
            @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value="pageSize",defaultValue = "5") Integer pageSize)
    {
        Page page = brandService.list(keyword, pageNum, pageSize);

        return CommonResult.success(CommonPage.restPage(page));
    }
}

