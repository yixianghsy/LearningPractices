/**
 *
 */
package com.xdclass.shop.service;

import com.xdclass.couponserviceapi.dto.CouponNoticeDto;
import com.xdclass.couponserviceapi.service.ICouponService;
import com.xdclass.shop.common.Page;
import com.xdclass.shop.model.News;
import com.xdclass.shop.repository.NewsRepository;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻模块Service
 *
 * @author Daniel
 */
@Service
@Transactional
public class NewsService extends BaseService {

    @Autowired
    NewsRepository newsDao;


    @Reference
    private ICouponService iCouponService;

    public void addNews(News news) {
        newsDao.save(news);
    }

    public void delNews(Integer newsId) {
        newsDao.delete(newsId);
    }

    public List<News> findNews(Page<News> page) {
        page.setResult(queryNotice());
        page.setTotalCount(10);
        return page.getResult();
    }

    public News findById(Integer id) {
        return newsDao.findOne(id);
    }


    public List<News> queryNotice() {
        List<CouponNoticeDto> dtos = iCouponService.queryCouponNotice();
        List<News> newsList = dtos.stream().map(dto -> {
            News news = new News();
            int userId = dto.getUserId();
            int reduce = dto.getReduceAmount();
            String title = dto.getTitle();
            String title1 = "恭喜" + userId + "使用" + title + "优惠券,获得减免金额" + reduce;
            news.setTitle(title1);
            news.setCreateTime(new Date());
            return news;
        }).collect(Collectors.toList());
        return newsList;
    }

}
