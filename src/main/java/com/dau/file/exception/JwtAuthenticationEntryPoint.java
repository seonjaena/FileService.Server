package com.dau.file.exception;

import com.dau.file.exception.exception.UnAuthenticatedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

/**
 * Spring Security 필터에서 401 Unauthorized가 발생할 경우 아래 코드로 진입
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 시스템에서 발생한 에러가 아니기 때문에 warning 레벨로 로그 출력
        log.warn("Not Authenticated user requested. ip={}, uri={}", request.getRemoteAddr(), request.getRequestURI());
        handlerExceptionResolver.resolveException(request, response, null, new UnAuthenticatedException(messageSource.getMessage("error.common.401", null, LocaleContextHolder.getLocale())));
    }

}
