package com.xdclass.shop.dto;

import com.alibaba.fastjson.JSON;
import com.xdclass.shop.model.UserCoupon;

/***
 * @author daniel
 */
public class UserCouponDTO extends UserCoupon {

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


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
