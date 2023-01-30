package com.mengxuegu.oauth2.web.controller;

import com.mengxuegu.base.result.MengxueguResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('product')")
    public MengxueguResult list(Principal principal) {
        List<String> list = new ArrayList<>();
        list.add("眼镜");
        list.add("格子衬衣");
        list.add("双肩包");
        list.add(principal.getName());
        return MengxueguResult.ok(list);
    }

}
