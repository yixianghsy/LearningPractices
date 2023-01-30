package com.xdclass.shop.controller;

import com.xdclass.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Daniel
 */
@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(ModelAndView model) {
        model = new ModelAndView("index");
        model.addObject("newProductList", productService.findNew());
        model.addObject("popProductList", productService.findPop());
        model.addObject("productList", productService.findAll());
        model.addObject("productTypeList", productService.findType());
        return model;
    }

}
