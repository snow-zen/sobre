package org.snowzen.annotation;

import java.lang.annotation.*;

/**
 * 请求过滤器，防止同一请求短时间内重复提交
 *
 * @author snow-zen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequestLimits.class)
@Documented
public @interface RequestLimit {

    /**
     * 指定请求频率，配合{@link #interval()}使用
     *
     * @return 请求频率
     */
    int frequency() default 0;

    /**
     * 指定时间间隔，配合{@link #frequency()}使用
     *
     * @return 时间间隔
     */
    int interval() default 0;
}
