package cn.bugstack.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 定义拦截注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "singleton";

}
