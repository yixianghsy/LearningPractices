package org.itstack.demo.design;
//活动服务接⼝
/**
 * 在这个静态类中提供了活动的查询和状态变更接
 * ⼝； queryActivityInfo 、 queryActivityStatus 、 execStatus 。
 * 同时使⽤Map的结构来记录活动ID和状态变化信息，另外还有init⽅法来初始化活动数据。实际的
 * 开发中这类信息基本都是从 数据库 或者 Redis 中获取。
 */

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityService {

    private static Map<String, Enum<Status>> statusMap = new ConcurrentHashMap<String, Enum<Status>>();

    public static void init(String activityId, Enum<Status> status) {
        // 模拟查询活动信息
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setActivityName("早起学习打卡领奖活动");
        activityInfo.setStatus(status);
        activityInfo.setBeginTime(new Date());
        activityInfo.setEndTime(new Date());
        statusMap.put(activityId, status);
    }

    /**
     * 查询活动信息
     *
     * @param activityId 活动ID
     * @return 查询结果
     */
    public static ActivityInfo queryActivityInfo(String activityId) {
        // 模拟查询活动信息
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId(activityId);
        activityInfo.setActivityName("早起学习打卡领奖活动");
        activityInfo.setStatus(statusMap.get(activityId));
        activityInfo.setBeginTime(new Date());
        activityInfo.setEndTime(new Date());
        return activityInfo;
    }

    /**
     * 查询活动状态
     *
     * @param activityId 活动ID
     * @return 查询结果
     */
    public static Enum<Status> queryActivityStatus(String activityId) {
        return statusMap.get(activityId);
    }

    /**
     * 执行状态变更
     *
     * @param activityId   活动ID
     * @param beforeStatus 变更前状态
     * @param afterStatus  变更后状态 b
     */
    public static synchronized void execStatus(String activityId, Enum<Status> beforeStatus, Enum<Status> afterStatus) {
        if (!beforeStatus.equals(statusMap.get(activityId))) return;
        statusMap.put(activityId, afterStatus);
    }

}
