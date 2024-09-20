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

    // controller가 실행되고 사용자에게 리턴되는 데이터에 추가적인 데이터를 삽입한다. (서버의 버전 등)
    @AfterReturning(pointcut = "execution(* com.dau.file.controller.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof CommonResponseDto<?>) {
            CommonResponseDto dto = (CommonResponseDto) result;
            dto.setMeta(projectInfo.getVersion());
        }
    }

}
