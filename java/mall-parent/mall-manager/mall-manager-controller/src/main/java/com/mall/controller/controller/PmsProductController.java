package com.mall.controller.controller;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageInfo;
import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.mansger.dto.PmsProductQueryParam;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "PmsProductController")
@Tag(name = "PmsProductController", description = "商品管理")
@RequestMapping("/product")
public class PmsProductController {
    @Reference
    private PmsProductService productService;
    @ApiOperation("查询商品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = productService.list(productQueryParam, pageSize, pageNum);
        PageInfo<PmsProduct> pageInfos = new PageInfo<>(productList);
        System.out.println("//第几页:"+pageInfos.getPageNum());
        System.out.println("//没页多少数据："+ pageInfos.getPageSize());
        System.out.println("//没页实际多少数据："+ pageInfos.getSize());
        System.out.println("//总共几页:"+pageInfos.getPages());
        System.out.println("//总共多少条数据:"+pageInfos.getTotal());
        System.out.println("//结果集:"+pageInfos.getList().get(0).toString());
        return CommonResult.success(CommonPage.restPage(pageInfos));



    }

}
