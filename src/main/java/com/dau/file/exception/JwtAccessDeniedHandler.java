package com.dau.file.exception;

import com.dau.file.exception.exception.UnAuthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

/**
 * Spring Security FilterでHttpStatus 403 Forbiddenが発生したとき下のコード実行
 * Filterで発生したエラーはControllerAdviceで処理できないからこういうふうにする
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MessageSource messageSource;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // システムの問題じゃないからwarningログでする
        log.warn("Not Authorized user requested. ip={}, uri={}, userId={}", request.getRemoteAddr(), request.getRequestURI(), request.getUserPrincipal().getName());
        handlerExceptionResolver.resolveException(request, response, null, new UnAuthorizedException(messageSource.getMessage("error.common.403", null, LocaleContextHolder.getLocale())));
    }
}
