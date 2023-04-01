package org.itstack.demo.design.test;

import org.itstack.demo.design.pay.channel.Pay;
import org.itstack.demo.design.pay.channel.WxPay;
import org.itstack.demo.design.pay.channel.ZfbPay;
import org.itstack.demo.design.pay.mode.PayFaceMode;
import org.itstack.demo.design.pay.mode.PayFingerprintMode;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 桥接模式的主要作⽤就是通过将抽象部分与实现部分分离，把多种可匹配的使⽤进⾏组合。说⽩了核⼼
 * 实现也就是在A类中含有B类接⼝，通过构造函数传递B类的实现，这个B类就是设计的 桥
 * 注：这不就是经常用到的码？
 */
public class ApiTest {

    @Test
    public void test_pay() {

        System.out.println("\r\n模拟测试场景；微信支付、人脸方式。");
        Pay wxPay = new WxPay(new PayFaceMode());
        wxPay.transfer("weixin_1092033111", "100000109893", new BigDecimal(100));

        System.out.println("\r\n模拟测试场景；支付宝支付、指纹方式。");
        Pay zfbPay = new ZfbPay(new PayFingerprintMode());
        zfbPay.transfer("jlu19dlxo111","100000109894",new BigDecimal(100));

    }

}
