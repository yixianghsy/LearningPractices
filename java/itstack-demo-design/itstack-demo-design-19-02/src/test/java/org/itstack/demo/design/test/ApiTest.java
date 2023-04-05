package org.itstack.demo.design.test;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.ActivityService;
import org.itstack.demo.design.Result;
import org.itstack.demo.design.StateHandler;
import org.itstack.demo.design.Status;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从以上的两种⽅式对⼀个需求的实现中可以看到，在第⼆种使⽤设计模式处理后已经没有了
 * ifelse ，代码的结构也更加清晰易于扩展。这就是设计模式的好处，可以⾮常强⼤的改变原有代
 * 码的结构，让以后的扩展和维护都变得容易些。
 * 在实现结构的编码⽅式上可以看到这不再是⾯向过程的编程，⽽是⾯向对象的结构。并且这样的设
 * 计模式满⾜了 单⼀职责 和 开闭原则 ，当你只有满⾜这样的结构下才会发现代码的扩展是容易的，
 * 也就是增加和修改功能不会影响整体的变化。
 * 但如果状态和各项流转较多像本⽂的案例中，就会产⽣较多的实现类。因此可能也会让代码的实现
 * 上带来了时间成本，因为如果遇到这样的场景可以按需评估投⼊回报率。主要点在于看是否经常修
 * 改、是否可以做成组件化、抽离业务与⾮业务功能
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_Editing2Arraignment() {
        String activityId = "100001";
        ActivityService.init(activityId, Status.Editing);

        StateHandler stateHandler = new StateHandler();
        Result result = stateHandler.arraignment(activityId, Status.Editing);

        logger.info("测试结果(编辑中To提审活动)：{}", JSON.toJSONString(result));
        logger.info("活动信息：{} 状态：{}", JSON.toJSONString(ActivityService.queryActivityInfo(activityId)), JSON.toJSONString(ActivityService.queryActivityInfo(activityId).getStatus()));
    }

    @Test
    public void test_Editing2Open() {
        String activityId = "100001";
        ActivityService.init(activityId, Status.Editing);

        StateHandler stateHandler = new StateHandler();
        Result result = stateHandler.open(activityId, Status.Editing);

        logger.info("测试结果(编辑中To开启活动)：{}", JSON.toJSONString(result));
        logger.info("活动信息：{} 状态：{}", JSON.toJSONString(ActivityService.queryActivityInfo(activityId)), JSON.toJSONString(ActivityService.queryActivityInfo(activityId).getStatus()));
    }

    @Test
    public void test_Refuse2Doing() {
        String activityId = "100001";
        ActivityService.init(activityId, Status.Refuse);

        StateHandler stateHandler = new StateHandler();
        Result result = stateHandler.doing(activityId, Status.Refuse);

        logger.info("测试结果(拒绝To活动中)：{}", JSON.toJSONString(result));
        logger.info("活动信息：{} 状态：{}", JSON.toJSONString(ActivityService.queryActivityInfo(activityId)), JSON.toJSONString(ActivityService.queryActivityInfo(activityId).getStatus()));
    }

    @Test
    public void test_Refuse2Revoke() {
        String activityId = "100001";
        ActivityService.init(activityId, Status.Refuse);

        StateHandler stateHandler = new StateHandler();
        Result result = stateHandler.checkRevoke(activityId, Status.Refuse);

        logger.info("测试结果(拒绝To撤审)：{}", JSON.toJSONString(result));
        logger.info("活动信息：{} 状态：{}", JSON.toJSONString(ActivityService.queryActivityInfo(activityId)), JSON.toJSONString(ActivityService.queryActivityInfo(activityId).getStatus()));
    }

}
