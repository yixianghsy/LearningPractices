package org.itstack.demo.design.agent;

import java.lang.annotation.*;

/**
 * 自定义查询注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Select {

    String value() default "";//sql语句

}
