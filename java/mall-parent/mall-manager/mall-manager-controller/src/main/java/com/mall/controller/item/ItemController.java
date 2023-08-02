package com.mall.controller.item;

import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.JsonUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品详情页面展示Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class  ItemController {
    @Reference
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        System.out.println("进来了");
        //调用服务取商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
//        Item item = new Item(tbItem);
        //取商品描述信息
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        //把信息传递给页面
        model.addAttribute("item",tbItem);
        model.addAttribute("itemDesc",tbItemDesc);
        //返回逻辑视图
        return "item";
    }
    /**
     * item-lits页面商品显示
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(@RequestParam(defaultValue="1")Integer page , @RequestParam(defaultValue="3")Integer rows){

        //调用服务查询商品列表
        EasyUIDataGridResult easyUIDataGridResult = itemService.getItemListgetItemList(page,rows);
        String json = JsonUtils.objectToJson(easyUIDataGridResult);
        System.out.println("ItemController.getItemList"+json);
        return easyUIDataGridResult;
    }
}
