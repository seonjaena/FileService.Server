package com.dau.file.extend;

import com.dau.file.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DtoMetaSetter {

    private final ProjectInfo projectInfo;

    @AfterReturning(pointcut = "execution(* com.dau.file.controller.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof CommonResponseDto<?>) {
            CommonResponseDto dto = (CommonResponseDto) result;
            dto.setMeta(projectInfo.getVersion());
        }
    }

}
