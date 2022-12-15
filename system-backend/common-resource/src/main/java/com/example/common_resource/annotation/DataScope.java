package com.example.common_resource.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataScope {

    String deptAlias() default "";

    String userAlias() default "";
}
