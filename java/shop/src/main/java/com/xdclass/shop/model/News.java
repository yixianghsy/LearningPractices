/**
 *
 */
package com.xdclass.shop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 新闻
 *
 * @author Daniel
 */
@Entity
@Table(name = "t_news")
public class News implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String title;
    private String content;
    private Date createTime;
    private Admin inputUser;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn
    public Admin getInputUser() {
        return inputUser;
    }

    public void setInputUser(Admin inputUser) {
        this.inputUser = inputUser;
    }
}
