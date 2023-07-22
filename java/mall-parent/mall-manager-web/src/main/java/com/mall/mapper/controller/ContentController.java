package com.mall.mapper.controller;
import com.mall.content.service.ContentService;
import com.mall.modules.content.TbContent;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 * <p>Title: ContentController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class ContentController {
    @Reference
    private ContentService contentService;
    @RequestMapping(value="/content/save", method= RequestMethod.POST)
    @ResponseBody
    public E3Result addContent(TbContent content){
            //调用服务把内容数据保存到数据库
        E3Result e3Result = contentService.addContent(content);
        return e3Result;
    }
}
