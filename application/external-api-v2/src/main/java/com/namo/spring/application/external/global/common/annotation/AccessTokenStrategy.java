package com.namo.spring.application.external.global.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

@Target(value = {
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("accessTokenStrategy")
public @interface AccessTokenStrategy {
}
