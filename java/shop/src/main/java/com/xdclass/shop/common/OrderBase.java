package com.xdclass.shop.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author daniel
 * @version 2020/12/15
 */
@Setter
@Getter
public class OrderBase implements Serializable {

    /**
     * 请求流水号
     */
    private String reqId;


}
