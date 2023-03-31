package org.itstack.demo.desgin.test;

import org.itstack.demo.design.QuestionBankController;
import org.junit.Test;

/**
 * 原型模式设计学习
 * 原型模式主要解决的问题就是创建重复对象，⽽这部分 对象 内容本身⽐较复杂，⽣成过程可能从库或者
 * RPC接⼝中获取数据的耗时较⻓
 */
public class ApiTest {

    @Test
    public void test_QuestionBank() throws CloneNotSupportedException {
        QuestionBankController questionBankController = new QuestionBankController();
        System.out.println(questionBankController.createPaper("花花", "1000001921032"));
        System.out.println(questionBankController.createPaper("豆豆", "1000001921051"));
        System.out.println(questionBankController.createPaper("大宝", "1000001921987"));
    }

}
