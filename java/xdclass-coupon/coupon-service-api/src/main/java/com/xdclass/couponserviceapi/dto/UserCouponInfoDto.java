package com.xdclass.couponserviceapi.dto;

import java.io.Serializable;

/**
 * @author daniel
 * 用户优惠券列表返回值
 */
public class UserCouponInfoDto extends UserCouponDto implements Serializable {

    /**
     * 优惠金额
     */
    private Integer reduceAmount;
    /**
     * 达到金额，如满500减50
     */
    private Integer achieveAmount;


    public Integer getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Integer reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Integer getAchieveAmount() {
        return achieveAmount;
    }

    public void setAchieveAmount(Integer achieveAmount) {
        this.achieveAmount = achieveAmount;
    }
}
