package com.fcannizzaro.oshare.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Francesco Cannizzaro (fcannizzaro)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Share {
    String value() default "null";
}
