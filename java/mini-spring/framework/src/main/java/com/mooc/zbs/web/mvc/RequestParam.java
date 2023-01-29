package com.mooc.zbs.web.mvc;

import java.lang.annotation.*;

/**
 * @author zbs
 * @date 2019/5/2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
}
