package com.mall.mapper.controller;
import com.mall.search.service.SearchItemService;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 设置重试次数和超时时间
     */
    @Reference(retries = 3,timeout = 2000)
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemList(){
        logger.debug("this is debug level");
        logger.info("this is info level ");
        logger.warn("this is warn level ");
        logger.error("this is error level");
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }
}
