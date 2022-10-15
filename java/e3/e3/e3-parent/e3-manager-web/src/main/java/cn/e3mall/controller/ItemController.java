package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    /**
     * PathVariable用于获取路径中的值{itemId}
     */
     public TbItem getItemById( @PathVariable Long itemId){
         TbItem tbItem  =itemService.getItemById(itemId);
        return tbItem;
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
        return easyUIDataGridResult;
    }
    /**
     * 商品添加功能
     */
    @RequestMapping(value="/item/save", method= RequestMethod.POST)
    public E3Result addItem( TbItem item, String desc){
        E3Result result = itemService.addItem(item, desc);
        return result;
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("/rest/item/delete")
    public E3Result deleteItemList(Long[] ids){
        E3Result e3Result = new E3Result();
        try{
            itemService.deleteItemList(ids);
            e3Result.setStatus(200);
        }catch (Exception e){
            e3Result.setStatus(500);
            throw new RuntimeException("删除失败", e);
        }

        return e3Result;
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
        E3Result e3Result = new E3Result();
        try{
            itemService.instockItemList(ids);
            e3Result.setStatus(200);
        }catch (Exception e){
            e3Result.setStatus(500);
            throw new RuntimeException("下架失败", e);
        }

        return e3Result;
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
        E3Result e3Result = new E3Result();
        try{
            itemService.reshelfItemList(ids);
            e3Result.setStatus(200);
        }catch (Exception e){
            e3Result.setStatus(500);
            throw new RuntimeException("下架失败", e);
        }

        return e3Result;
    }
}
