package com.mooc.zbs;

import com.mooc.zbs.starter.MiniApplication;

/**
 * @author zbs 2019/4/13
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        MiniApplication.run(Application.class, args);
    }
}
