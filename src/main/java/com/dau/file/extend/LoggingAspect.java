package com.dau.file.extend;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    SimpleDateFormat loggingTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");

    @Around("execution(* com.dau.file.controller.*.*(..))")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method: {}, Start Time: {}, End Time: {}, Duration: {} ms",
                joinPoint.getSignature().toShortString(), loggingTimeFormat.format(new Date(startTime)), loggingTimeFormat.format(new Date(endTime)), (endTime - startTime));
        return result;
    }

}
