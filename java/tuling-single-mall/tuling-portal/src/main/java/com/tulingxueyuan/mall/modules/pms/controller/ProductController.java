package com.tulingxueyuan.mall.modules.pms.controller;

import com.tulingxueyuan.mall.common.api.CommonResult;
import com.tulingxueyuan.mall.dto.ProductDetailDTO;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@Api(tags = "ProductController",description = "商品详情服务接口")
@RequestMapping("/product")
public class ProductController {


    @Autowired
    PmsProductService productService;

    /**
     *  取商品详情获
     * .get(`/product/detail/${this.id}`)
     */
    @RequestMapping("/detail/{id}")
    public CommonResult getProductDetail(@PathVariable Long id){
        ProductDetailDTO productDetailDTO= productService.getProductDetail(id);
        return CommonResult.success(productDetailDTO);
    }
}
