package com.mooc.zbs.beans;

import java.lang.annotation.*;

/**
 * @author zbs
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
