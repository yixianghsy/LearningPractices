package com.xdclass.shop.model;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Daniel
 */
@Entity
@DynamicUpdate
@Table(name = "t_user_coupon")
public class UserCoupon implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer couponId;
    private Integer userId;
    private Date createTime;//创建时间
    private String userCouponCode;//商品编码
    private String picUrl;//主图
    private Integer orderId;
    private Integer status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Lob
    @Column(name = "pic_url")
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Column(name = "coupon_id")
    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "user_coupon_code")
    public String getUserCouponCode() {
        return userCouponCode;
    }

    public void setUserCouponCode(String userCouponCode) {
        this.userCouponCode = userCouponCode;
    }

    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserCoupon{" +
                "id=" + id +
                ", couponId=" + couponId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", userCouponCode='" + userCouponCode + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
