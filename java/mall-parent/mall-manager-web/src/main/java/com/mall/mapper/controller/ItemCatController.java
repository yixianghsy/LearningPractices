package com.mall.mapper.controller;
import com.mall.pojo.EasyUITreeNode;
import com.mall.mansger.service.ItemCatService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 商品分类管理Controller
 * <p>Title: ItemCatController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class ItemCatController {
    @Reference
    private ItemCatService itemCatService;

    /**
     * 商品类目选择
     * @param parentId
     * @return
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id",defaultValue = "0") Long parentId){
        //调用服务查询节点列表
        List<EasyUITreeNode> list  =itemCatService.getItemCatlist(parentId);
        return list;
    }
}
