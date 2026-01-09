package com.lbs.blaybus.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드 또는 클래스의 실행 시간을 측정하는 어노테이션
 * 사용법:
 * - @TimeTrace: 메서드에 붙이면 해당 메서드 실행 시간 측정
 * - @TimeTrace: 클래스에 붙이면 모든 public 메서드 실행 시간 측정
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)  // CLASS -> RUNTIME으로 변경 (AOP 동작을 위해)
public @interface TimeTrace {
    String value() default "";
}