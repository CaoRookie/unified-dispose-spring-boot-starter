package com.cebon.tool.dispose.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.cebon.tool.dispose.config.GlobalDefaultConfiguration;

/**
 * @author cy
 * @date 2019-11-13 12:40
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(GlobalDefaultConfiguration.class)
public @interface EnableGlobalDispose {}
