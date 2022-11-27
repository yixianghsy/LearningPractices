package com.tulingxueyuan.mall.modules.pms.controller;


import com.tulingxueyuan.mall.common.api.CommonPage;
import com.tulingxueyuan.mall.common.api.CommonResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tulingxueyuan.mall.dto.ProductConditionDTO;
import com.tulingxueyuan.mall.dto.ProductSaveParamsDTO;
import com.tulingxueyuan.mall.dto.ProductUpdateInitDTO;
import com.tulingxueyuan.mall.modules.pms.model.PmsProduct;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author XuShu
 * @since 2022-11-16
 */
@RestController
@RequestMapping("/product")
public class PmsProductController {
    @Autowired
    PmsProductService productService;

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
    public CommonResult list(ProductConditionDTO condition){
        Page page= productService.list(condition);
        return CommonResult.success(CommonPage.restPage(page));
    }
    /**
     *  url:'/product/update/deleteStatus',
     *     method:'post',
     *     params:params
     */
    @RequestMapping(value="/update/deleteStatus",method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        boolean result = productService.removeByIds(ids);
        if (result){
            return  CommonResult.success(result);

        }else {
            return CommonResult.failed();
        }

    }
    /**
     *  更新是否新品状态
     export function updateNewStatus(params) a {
     return request({
     url:'/product/update/newStatus',
     method:'post',
     params:params
     })
     } */

    @RequestMapping(value="/update/newStatus",method = RequestMethod.POST)
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        boolean result = productService.updateStatus(newStatus, ids, PmsProduct::getNewStatus);
        if (result){
            return  CommonResult.success(result);
        }else {
            return  CommonResult.failed();
        }

    }

    /**
     *  更新是否推荐
     export function updateRecommendStatus(params) {
     return request({
     url:'/product/update/recommendStatus',
     method:'post',
     params:params
     })
     }*/

    @RequestMapping(value="/update/recommendStatus",method = RequestMethod.POST)
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("recommendStatus") Integer recommendStatus) {
        boolean result = productService.updateStatus(recommendStatus, ids, PmsProduct::getRecommandStatus);
        if (result){
            return  CommonResult.success(result);

        }else {
            return CommonResult.failed();
        }
    }
    /**
     *
     * 更新是否上架
     export function updatePublishStatus(params) {
     return request({
     url:'/product/update/publishStatus',
     method:'post',
     params:params
     })
     }
     */
    @RequestMapping(value="/update/publishStatus",method = RequestMethod.POST)
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus") Integer publishStatus) {
        boolean result= productService.updateStatus(publishStatus,ids,PmsProduct::getPublishStatus);
        if(result){
            return CommonResult.success(result);
        }
        else {
            return CommonResult.failed();
        }
    }

    /**
     *  商品添加
     * url:'/product/create',
     *     method:'post',
     *     data:data    json
     */
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public CommonResult create(@RequestBody ProductSaveParamsDTO productSaveParamsDTO){
        boolean result = productService.create(productSaveParamsDTO);
        if (result){
            return  CommonResult.success(result);

        }else {
            return  CommonResult.failed();
        }
    }
    /**
     *  获取编辑状态下商品信息
     *  url:'/product/updateInfo/'+id,
     *     method:'get',
     */
    @RequestMapping(value="/updateInfo/{id}")
    public CommonResult getUpdateInfo(@PathVariable Long id){
        ProductUpdateInitDTO updateInitDTO = productService.getUpdateInfo(id);
        return CommonResult.success(updateInitDTO);
    }

    /**
     *  商品修改—保存
     *  url:'/product/update/'+id,
     *     method:'post',
     *     data:data   json
     */
    @RequestMapping(value="/update/{id}",method = RequestMethod.POST)
    public CommonResult update(@RequestBody @Valid ProductSaveParamsDTO productSaveParamsDTO){
        boolean result = productService.update(productSaveParamsDTO);
        if (result){
            return CommonResult.success(result);

        }else {
            return  CommonResult.failed();
        }
    }
}

