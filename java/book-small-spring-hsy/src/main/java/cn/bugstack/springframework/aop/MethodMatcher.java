package cn.bugstack.springframework.aop;

import java.lang.reflect.Method;

/**
 * Part of a {@link Pointcut}: Checks whether the target method is eligible for advice.
 *方法匹配。用于找到表达式方位内匹配得目标类和方法
 * Create by 小傅哥(fustack)
 */
public interface MethodMatcher {
    /**
     * Perform static checking whether the given method matches. If this
     * @return whether or not this method matches statically
     */
    boolean matches(Method method, Class<?> targetClass);
}
