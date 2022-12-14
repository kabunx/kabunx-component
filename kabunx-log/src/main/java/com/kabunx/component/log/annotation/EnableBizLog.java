package com.kabunx.component.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableBizLog {

    String basePackage() default "com.kabunx";

    String[] pointcuts() default {};
}
