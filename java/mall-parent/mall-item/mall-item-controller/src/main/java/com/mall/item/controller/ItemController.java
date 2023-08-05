package com.mall.item.controller;



import com.mall.item.service.ItemService;
import com.mall.item.vo.Item;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
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
    @RequestMapping("/item/{itemId}")
        public String showItemInfo(@PathVariable Long itemId, Model model) {
        System.out.println("进来了");
        //调用服务取商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);

        Item item = new Item(tbItem);
        //取商品描述信息
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        //把信息传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        //返回逻辑视图
        return "item";
    }

}
