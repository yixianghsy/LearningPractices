package com.xdclass.shop.model;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Daniel
 */
@Entity
@DynamicUpdate
@Table(name = "t_coupon")
public class Coupon implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String title;//名称
    private Integer reduceAmount;//优惠金额
    private Integer achieveAmount;//达到金额，如满500减50
    private Date createTime;//创建时间
    private String code;//商品编码
    private Long stock;//库存
    private String picUrl;//主图
    private Integer status;//兑换状态

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "reduce_amount")
    public Integer getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Integer reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    @Column(name = "achieve_amount")
    public Integer getAchieveAmount() {
        return achieveAmount;
    }

    public void setAchieveAmount(Integer achieveAmount) {
        this.achieveAmount = achieveAmount;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
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


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", reduceAmount=" + reduceAmount +
                ", achieveAmount=" + achieveAmount +
                ", createTime=" + createTime +
                ", code='" + code + '\'' +
                ", stock=" + stock +
                ", picUrl='" + picUrl + '\'' +
                ", status=" + status +
                '}';
    }
}
