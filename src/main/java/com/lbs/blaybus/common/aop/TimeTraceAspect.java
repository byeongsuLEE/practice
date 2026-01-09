package com.lbs.blaybus.common.aop;

import com.lbs.blaybus.common.aop.TimeTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * TimeTrace 어노테이션이 붙은 메서드의 실행 시간을 측정하는 AOP
 *
 * 동작 방식:
 * 1. 메서드 실행 전 시작 시간 기록
 * 2. 메서드 실행
 * 3. 메서드 실행 후 종료 시간 기록 및 실행 시간 계산
 * 4. 로그 출력
 */
@Slf4j
@Aspect
@Component
public class TimeTraceAspect {

    @Around("@annotation(timeTrace) || @within(timeTrace)")
    public Object traceTime(ProceedingJoinPoint joinPoint, TimeTrace timeTrace) throws Throwable {
        long start = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            log.info("[TimeTrace] {}.{} 시작", className, methodName);
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - start;
            log.info("[TimeTrace] {}.{} 종료 - 실행시간: {}ms", className, methodName, executionTime);
        }
    }
}