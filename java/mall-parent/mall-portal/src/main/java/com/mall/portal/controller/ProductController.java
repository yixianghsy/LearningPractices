package com.mall.portal.controller;

import com.mall.api.CommonResult;
import com.mall.mansger.service.PmsProductService;
import com.mall.mansger.dto.ProductDetailDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Reference
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
