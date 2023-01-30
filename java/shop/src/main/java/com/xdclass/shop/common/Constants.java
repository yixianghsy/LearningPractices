/**
 *
 */
package com.xdclass.shop.common;

/**
 * @author Daniel
 * @date 2019-7-18
 */
public class Constants {
    public static final String LOGIN_USER = "login_user";
    public static final String LOGIN_ADMIN = "login_admin";
    public static final String CART = "cart";
    public static final int IMG_WIDTH = 300;
    public static final int IMG_HEIGHT = 300;

    public static class OrderStatus {
        public static final Integer WAIT_PAY = 0;//代付款
        public static final Integer PAYED = 1;//已付款
        public static final Integer SHIPPED = 2;//已发货
        public static final Integer DELETED = 3;//已删除
        public static final Integer ENDED = 4;//已完成
    }
}
