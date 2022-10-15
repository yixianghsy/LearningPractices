package com.e3mall.portal.controller;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.pojo.TbContent;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 首页展示Controller
 * <p>Title: IndexController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class IndexController {
    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;
   @Reference
    private ContentService contentService;
    @RequestMapping("/index")
    public String showIndex(Model model){
        //查询内容列表
        List<TbContent> list= contentService.getContentListByCid(CONTENT_LUNBO_ID);
         model.addAttribute("ad1List", list);
        return "index";
    }
}
