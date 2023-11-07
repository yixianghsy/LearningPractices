//package com.mall.portal.controller;
//
//import com.mall.api.CommonPage;
//import com.mall.api.CommonResult;
//import com.mall.portal.domain.EsProduct;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
///**
// * 搜索商品管理Controller
// * Created on 2018/6/19.
// */
//@Controller
//@Api(tags = "EsProductController", description = "搜索商品管理")
//@RequestMapping("/esProduct")
//public class EsProductController {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @ApiOperation(value = "简单搜索")
//    @RequestMapping(value = "/search/simple", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
//                                                      @RequestParam(required = false, defaultValue = "0") String pageNum,
//                                                      @RequestParam(required = false, defaultValue = "10") String pageSize) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("keyword", keyword);
//        map.add("pageNum", pageNum);
//        map.add("pageSize", pageSize);
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//        try {
//            ResponseEntity<CommonResult> result = restTemplate.postForEntity("http://localhost:8085/esProduct/search/simple",request,CommonResult.class);
//            return result.getBody();
//        } catch (RestClientException e) {
//            e.printStackTrace();
//        }
//        return CommonResult.failed();
//    }
//
//}
