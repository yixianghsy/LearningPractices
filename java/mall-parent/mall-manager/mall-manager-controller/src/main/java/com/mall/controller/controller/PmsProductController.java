package com.mall.controller.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/product")
public class PmsProductController {

    /**
     *
     url:'/product/list',
     method:'get',
     data:          axios 如果设置的是data属性就是以json的方式传递
     params:{       axios 如果设置的是params属性就是以url参数的方式传递
     如果传递是URLSearchParams  会以formdata的方式传递
     keyword: null,
     pageNum: 1,
     pageSize: 5,
     publishStatus: null,
     verifyStatus: null,
     productSn: null,
     productCategoryId: null,
     brandId: null
     };
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public void list(){
        System.out.println("进来了");
    }
}
