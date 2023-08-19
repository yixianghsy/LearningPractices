package com.mall.controller.item;

import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;
import com.mall.utils.JsonUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 商品详情页面展示Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
@RequestMapping("/item")
public class  ItemController {
    @Reference
    private ItemService itemService;
    @GetMapping("/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }
    /**
     * item-lits页面商品显示
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }
    /**
     * 商品添加功能
     */
    @RequestMapping("/save")
    @ResponseBody
    public E3Result saveItem(TbItem item, String desc) {
        E3Result result = itemService.addItem(item, desc);
        return result;
    }
    //以下功能是自己加的

    /**
     * 删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/delete")
    public E3Result deleteItemList(Long[] ids){
        E3Result Result = new E3Result();
        try{
            itemService.deleteItemList(ids);
            Result.setStatus(200);
        }catch (Exception e){
            Result.setStatus(500);
            throw new RuntimeException("删除失败", e);
        }

        return Result;
    }

    /**
     * 编辑-加载商品描述
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/query/item/desc")
    public Integer queryItemList(String id){
        System.out.println("进来了"+id);
        return 200;
    }

    /**
     * 编辑-加载商品规格
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/param/item/query")
    public Integer queryParamItemList(String id){
        System.out.println("进来了"+id);
        return 200;
    }
    /**
     * 下架
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/instock")
    public E3Result instockItemList(Long[] ids){
        E3Result Result = new E3Result();
        try{
            itemService.instockItemList(ids);
            Result.setStatus(200);
        }catch (Exception e){
            Result.setStatus(500);
            throw new RuntimeException("下架失败", e);
        }

        return Result;
    }

    /**
     * 上架
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/reshelf")
    public E3Result reshelfItemList(Long[] ids){
        E3Result Result = new E3Result();
        try{
            itemService.reshelfItemList(ids);
            Result.setStatus(200);
        }catch (Exception e){
            Result.setStatus(500);
            throw new RuntimeException("下架失败", e);
        }

        return Result;
    }
}
