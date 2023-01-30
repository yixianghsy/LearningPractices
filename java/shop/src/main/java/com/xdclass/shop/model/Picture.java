package com.xdclass.shop.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 图片实体
 *
 * @author daniel
 */
@Entity
@DynamicUpdate
@Table(name = "t_picture")
public class Picture {
    private Integer id;
    private String title;
    private String memo;
    private String url;
    private Date updateTime;
    private Admin updateAdmin;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne
    @JoinColumn
    public Admin getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(Admin updateAdmin) {
        this.updateAdmin = updateAdmin;
    }
}
