package com.xdclass.couponapp.dto;

public class OrderCouponDto {

    private String couponCode;
    private Integer orderId;
    private Integer userId;
    //0代表用户支付成功，1代表退款，2代表下单成功
    private Integer status;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderCouponDto{" +
                "couponCode='" + couponCode + '\'' +
                ", orderId=" + orderId +
                ", userId=" + userId +
                '}';
    }

    public OrderCouponDto(String couponCode, Integer orderId, Integer userId) {
        this.couponCode = couponCode;
        this.orderId = orderId;
        this.userId = userId;
        this.status=2;
    }

    public OrderCouponDto(Integer orderId, Integer userId, Integer status) {
        this.orderId = orderId;
        this.userId = userId;
        this.status=status;
    }

    public OrderCouponDto() {
    }
}
