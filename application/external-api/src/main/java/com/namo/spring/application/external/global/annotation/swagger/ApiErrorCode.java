package com.namo.spring.application.external.global.annotation.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.namo.spring.core.common.code.status.ErrorStatus;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCode {

	ErrorStatus value();

}
