package org.itstack.demo.design.test;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.LotteryResult;
import org.itstack.demo.design.LotteryService;
import org.itstack.demo.design.LotteryServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从我们最基本的过程式开发以及后来使⽤观察者模式⾯向对象开发，可以看到设计模式改造后，拆
 * 分出了核⼼流程与辅助流程的代码。⼀般代码中的核⼼流程不会经常变化。但辅助流程会随着业务
 * 的各种变化⽽变化，包括； 营销 、 裂变 、 促活 等等，因此使⽤设计模式架设代码就显得⾮常有必
 * 要。
 * 此种设计模式从结构上是满⾜开闭原则的，当你需要新增其他的监听事件或者修改监听逻辑，是不
 * 需要改动事件处理类的。但是可能你不能控制调⽤顺序以及需要做⼀些事件结果的返回继续操作，
 * 所以使⽤的过程时需要考虑场景的合理性。
 * 任何⼀种设计模式有时候都不是单独使⽤的，需要结合其他模式共同建设。另外设计模式的使⽤是
 * 为了让代码更加易于扩展和维护，不能因为添加设计模式⽽把结构处理更加复杂以及难以维护。这
 * 样的合理使⽤的经验需要⼤量的实际操作练习⽽来
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() {
        LotteryService lotteryService = new LotteryServiceImpl();
        LotteryResult result = lotteryService.draw("2765789109876");
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

}
