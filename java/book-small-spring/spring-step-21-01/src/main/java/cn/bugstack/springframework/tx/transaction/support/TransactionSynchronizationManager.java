package cn.bugstack.springframework.tx.transaction.support;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @description Central delegate that manages resources and transaction synchronizations per
 * subscriber context.
 * @date 2022/3/16
 *  /CodeDesignTutorials
 *
 */
public abstract class TransactionSynchronizationManager {

    /**
     * 当前线程的连接存储
     */
    private static final ThreadLocal<Map<Object, Object>> resources = new NamedThreadLocal<>("Transactional resources");

    /**
     * 事务的名称
     */
    private static final ThreadLocal<String> currentTransactionName = new NamedThreadLocal<>("Current transaction name");

    /**
     * 事务是否是只读
     */
    private static final ThreadLocal<Boolean> currentTransactionReadOnly = new NamedThreadLocal<>("Current transaction read-only status");

    /**
     * 事务的隔离级别
     */
    private static final ThreadLocal<Integer> currentTransactionIsolationLevel = new NamedThreadLocal<>("Current transaction isolation level");

    /**
     * 事务是否开启
     */
    private static final ThreadLocal<Boolean> actualTransactionActive = new NamedThreadLocal<>("Actual transaction active");

    public static Object getResource(Object key) {
        return doGetResource(key);
    }

    private static Object doGetResource(Object actualKey) {
        Map<Object, Object> map = resources.get();
        if (null == map) {
            return null;
        }
        return map.get(actualKey);
    }

    public static void bindResource(Object key, Object value) throws IllegalStateException {
        Assert.notNull(value, "Value must not be null");
        Map<Object, Object> map = resources.get();
        if (null == map) {
            map = new HashMap<>();
            resources.set(map);
        }
        map.put(key, value);
    }

}
