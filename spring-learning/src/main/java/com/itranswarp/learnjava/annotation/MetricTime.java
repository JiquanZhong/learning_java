package com.itranswarp.learnjava.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ZHONG Jiquan
 * @create 04/08/2023 - 00:08
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface MetricTime {
	String value();
}
