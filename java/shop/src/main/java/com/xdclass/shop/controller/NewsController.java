package com.xdclass.shop.controller;

import com.xdclass.shop.common.Page;
import com.xdclass.shop.model.News;
import com.xdclass.shop.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Daniel
 *
 */
@Controller
@RequestMapping("/news")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(ModelAndView model, HttpServletRequest request) {
        Page<News> page = new Page<News>(request);
        newsService.findNews(page);
        model.addObject("page", page);
        model.setViewName("news/newsList");
        return model;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewNews(@PathVariable Integer id, ModelAndView model, HttpServletRequest request) {
        News news = newsService.findById(id);
        model.addObject("news", news);
        model.setViewName("news/newsDetail");
        return model;
    }


    @ResponseBody
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public List<News> newsTest(ModelAndView model, HttpServletRequest request) {
        return newsService.queryNotice();
    }
}
