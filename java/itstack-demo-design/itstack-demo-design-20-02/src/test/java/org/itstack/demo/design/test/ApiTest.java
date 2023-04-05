package org.itstack.demo.design.test;

import org.itstack.demo.design.Context;
import org.itstack.demo.design.impl.MJCouponDiscount;
import org.itstack.demo.design.impl.NYGCouponDiscount;
import org.itstack.demo.design.impl.ZJCouponDiscount;
import org.itstack.demo.design.impl.ZKCouponDiscount;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack) @2020
 * 策略模式是⼀种⾏为模式，也是替代⼤量 ifelse 的利器。它所能帮你解决的是场景，⼀般是具有同类
 * 可替代的⾏为逻辑算法场景。⽐如；不同类型的交易⽅式(信⽤卡、⽀付宝、微信)、⽣成唯⼀ID策略
 * (UUID、DB⾃增、DB+Redis、雪花算法、Leaf算法)等，都可以使⽤策略模式进⾏⾏为包装，供给外部
 * 使⽤。
 * 以上的策略模式案例相对来说不并不复杂，主要的逻辑都是体现在关于不同种类优惠券的计算折扣
 * 策略上。结构相对来说也⽐较简单，在实际的开发中这样的设计模式也是⾮常常⽤的。另外这样的
 * 设计与命令模式、适配器模式结构相似，但是思路是有差异的。
 * 通过策略设计模式的使⽤可以把我们⽅法中的if语句优化掉，⼤量的if语句使⽤会让代码难以扩
 * 展，也不好维护，同时在后期遇到各种问题也很难维护。在使⽤这样的设计模式后可以很好的满⾜
 * 隔离性与和扩展性，对于不断新增的需求也⾮常⽅便承接。
 * 策略模式 、 适配器模式 、 组合模式 等，在⼀些结构上是⽐较相似的，但是每⼀个模式是有⾃⼰的
 * 逻辑特点，在使⽤的过程中最佳的⽅式是经过较多的实践来吸取经验，为后续的研发设计提供更好
 * 的技术输出
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_zj() {
        // 直减；100-10，商品100元
        Context<Double> context = new Context<Double>(new ZJCouponDiscount());
        BigDecimal discountAmount = context.discountAmount(10D, new BigDecimal(100));
        logger.info("测试结果：直减优惠后金额 {}", discountAmount);
    }

    @Test
    public void test_mj() {
        // 满100减10，商品100元
        Context<Map<String,String>> context = new Context<Map<String,String>>(new MJCouponDiscount());
        Map<String,String> mapReq = new HashMap<String, String>();
        mapReq.put("x","100");
        mapReq.put("n","10");
        BigDecimal discountAmount = context.discountAmount(mapReq, new BigDecimal(100));
        logger.info("测试结果：满减优惠后金额 {}", discountAmount);
    }


    @Test
    public void test_zk() {
        // 折扣9折，商品100元
        Context<Double> context = new Context<Double>(new ZKCouponDiscount());
        BigDecimal discountAmount = context.discountAmount(0.9D, new BigDecimal(100));
        logger.info("测试结果：折扣9折后金额 {}", discountAmount);
    }

    @Test
    public void test_nyg() {
        // n元购；100-10，商品100元
        Context<Double> context = new Context<Double>(new NYGCouponDiscount());
        BigDecimal discountAmount = context.discountAmount(90D, new BigDecimal(100));
        logger.info("测试结果：n元购优惠后金额 {}", discountAmount);
    }

}
