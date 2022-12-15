package com.example.common_resource.annotation;

import com.example.common_resource.enums.DataType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

    DataType value() default DataType.MASTER;
}
