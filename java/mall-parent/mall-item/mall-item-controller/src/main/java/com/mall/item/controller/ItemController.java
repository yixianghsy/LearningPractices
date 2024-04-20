package com.mall.item.controller;



import com.mall.item.service.ItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页面展示Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class ItemController {
    @Reference
    private ItemService itemService;
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        return null;
    }

}
