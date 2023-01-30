/**
 *
 */
package com.xdclass.shop.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 支付方式
 *
 * @author Daniel
 */
@Entity
@Table(name = "t_payment")
public class Payment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
