package com.mall.portal.controller;
import com.mall.content.service.ContentService;
import com.mall.modules.content.TbContent;
import org.apache.dubbo.config.annotation.Reference;
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
@Deprecated
@Controller
public class IndexController {

    @Value("${CONTENT_BANNER_ID}")
    private Long CONTENT_BANNER_ID;

    @Reference
    private ContentService contentService;

    @RequestMapping({"/index", "/", "index.html"})
    public String showIndex(Model model) {
        List<TbContent> contentList = contentService.getContentList(CONTENT_BANNER_ID);
        model.addAttribute("ad1List", contentList);
        return "index";
    }
}
