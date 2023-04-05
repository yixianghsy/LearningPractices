package org.itstack.demo.design.test;

import org.itstack.demo.design.NetMall;
import org.itstack.demo.design.impl.JDNetMall;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过上⾯的实现可以看到 模版模式 在定义统⼀结构也就是执⾏标准上⾮常⽅便，也就很好的控制
 * 了后续的实现者不⽤关⼼调⽤逻辑，按照统⼀⽅式执⾏。那么类的继承者只需要关⼼具体的业务逻
 * 辑实现即可。
 * 另外模版模式也是为了解决⼦类通⽤⽅法，放到⽗类中设计的优化。让每⼀个⼦类只做⼦类需要完
 * 成的内容，⽽不需要关⼼其他逻辑。这样提取公⽤代码，⾏为由⽗类管理，扩展可变部分，也就⾮
 * 常有利于开发拓展和迭代。
 * 但每⼀种设计模式都有⾃⼰的特定场景，如果超过场景外的建设就需要额外考虑
 * "
 * 其他模式的运
 * ⽤。⽽不是⾮要⽣搬硬套，否则⾃⼰不清楚为什么这么做，也很难让后续者继续维护代码。⽽想要
 * 活学活⽤就需要多加练习，有实践的经历
 */
public class ApiTest {

    public Logger logger = LoggerFactory.getLogger(ApiTest.class);

    /**
     * 测试链接
     * 京东；https://item.jd.com/100008348542.html
     * 淘宝；https://detail.tmall.com/item.htm
     * 当当；http://product.dangdang.com/1509704171.html
     */
    @Test
    public void test_NetMall() {
        NetMall netMall = new JDNetMall("1000001","*******");
        String base64 = netMall.generateGoodsPoster("https://item.jd.com/100008348542.html");
        logger.info("测试结果：{}", base64);
    }

}
