package com.xdclass.couponapp.service.schedule;

import com.xdclass.couponapp.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UpdateCouponJob {


    private static final Logger logger = LoggerFactory.getLogger(UpdateCouponJob.class);

    @Resource
    private CouponService couponService;

    @Scheduled(cron = "* 0/5 * * * ?")
    public void updateCoupon() {
        logger.info("enter update coupon job");
        couponService.updateCouponMap();
    }

}
