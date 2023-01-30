package com.xdclass.shop.controller;

import com.xdclass.shop.common.web.JsonResult;
import com.xdclass.shop.model.Product;
import com.xdclass.shop.service.ProductService;
import com.xdclass.shop.util.CartUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author Daniel
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String cart() {
        return "order/cart";
    }

    @RequestMapping(value = "/add/{id}/{total}")
    @ResponseBody
    public JsonResult addToCart(@PathVariable Integer id, @PathVariable Integer total, HttpSession session) {
        Product product = productService.findById(id);
        CartUtil.saveProductToCart(session, product, total);
        logger.debug("添加到购物车成功...");
        JsonResult result = new JsonResult();
        result.setToSuccess();
        return result;
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public JsonResult deleteFromCart(ModelAndView model, @PathVariable(value = "id") Integer productId, HttpSession session) {
        CartUtil.deleteProductFromCart(session, productId);
        logger.debug("购物车商品删除成功...");

        JsonResult result = new JsonResult();
        result.setToSuccess();
        return result;
    }

    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public JsonResult deleteAllFromCart(HttpSession session) {
        CartUtil.cleanCart(session);
        logger.debug("购物车商品清空成功...");

        JsonResult result = new JsonResult();
        result.setToSuccess();
        return result;
    }
}
