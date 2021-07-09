package org.snowzen.annotation;

import java.lang.annotation.*;

/**
 * @author snow-zen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimits {

    RequestLimit[] value();
}
