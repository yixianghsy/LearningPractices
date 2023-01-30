package com.xdclass.couponserviceapi.service;

import com.xdclass.couponserviceapi.dto.CouponDto;
import com.xdclass.couponserviceapi.dto.CouponNoticeDto;
import com.xdclass.couponserviceapi.dto.UserCouponDto;
import com.xdclass.couponserviceapi.dto.UserCouponInfoDto;

import java.util.List;


public interface ICouponService {

    /***
     * 获取有效时间的可用优惠券列表
     * // 1、是否存在远程调用 HTTP、RPC Metrics
     * // 2、大量内存处理  list.contain() ==>set.contain
     * @return
     */
    public List<CouponDto> getCouponList();

    public String saveUserCoupon(UserCouponDto dto);

    public List<UserCouponInfoDto> userCouponList(Integer userId);

    /**
     * 查询coupon公告栏,前10条数据  userId_couponId  ==>  1_1==> string[]  string[0] =1,string[1] =1
     */
    public List<CouponNoticeDto> queryCouponNotice();
}
