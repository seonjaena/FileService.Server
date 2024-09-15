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
 * Spring Security FilterでHttpStatus 401 Unauthorizedが発生したとき下のコード実行
 * Filterで発生したエラーはControllerAdviceで処理できないからこういうふうにする
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // システムの問題じゃないからwarningログでする
        log.warn("Not Authenticated user requested. ip={}, uri={}", request.getRemoteAddr(), request.getRequestURI());
        handlerExceptionResolver.resolveException(request, response, null, new UnAuthenticatedException(
                messageSource.getMessage("error.common.401",
                        new String[] {request.getRemoteAddr(), request.getRequestURI()},
                        LocaleContextHolder.getLocale())));
    }

}
