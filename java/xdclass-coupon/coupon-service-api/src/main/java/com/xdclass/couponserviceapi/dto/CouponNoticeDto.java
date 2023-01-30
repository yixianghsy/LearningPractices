package com.xdclass.couponserviceapi.dto;

import java.io.Serializable;
import java.util.Date;

public class CouponNoticeDto implements Serializable {
    private Integer id;

    private String code;

    private String picUrl;

    private Integer achieveAmount;

    private Integer reduceAmount;

    private Integer stock;

    private String title;

    private Integer status;

    private Integer userId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getAchieveAmount() {
        return achieveAmount;
    }

    public void setAchieveAmount(Integer achieveAmount) {
        this.achieveAmount = achieveAmount;
    }

    public Integer getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Integer reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CouponNoticeDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", achieveAmount=" + achieveAmount +
                ", reduceAmount=" + reduceAmount +
                ", stock=" + stock +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }
}
