package com.mall.controller;
import com.mall.api.CommonPage;
import com.mall.api.CommonResult;
import com.mall.mansger.dto.PmsProductQueryParam;
import com.mall.mansger.model.PmsProduct;
import com.mall.mansger.service.PmsProductService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
//@Api(tags = "PmsProductController")
//@Tag(name = "PmsProductController", description = "商品管理")
@RequestMapping("/product")
public class PmsProductController {
    @Reference
    private PmsProductService productService;
//    @ApiOperation("查询商品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        CommonPage orderlist = productService.list(productQueryParam, pageSize, pageNum);
        return CommonResult.success(orderlist);

    }

}
