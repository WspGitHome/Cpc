package com.wanfangdata.cpc.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {

    /**
     * 业务的名称
     */
    String value() default "";

    /**
     * redis缓存的Key（默认类名-方法名-自定义key）
     */
    String key() default "";

    /**
     * 是否刷新缓存，默认false
     */
    boolean flush() default false;

    /**
     * 缓存失效时间，默认30
     */
    long expire() default 30L;

    /**
     * 缓存时间单位，默认天
     */
    TimeUnit unit() default TimeUnit.DAYS;
}
