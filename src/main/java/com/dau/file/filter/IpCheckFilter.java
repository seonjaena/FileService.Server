package com.dau.file.filter;

import com.dau.file.exception.exception.UnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpCheckFilter extends OncePerRequestFilter {

    @Value("${service.allowed-ips:#{null}}")
    private final List<String> allowedIps;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();

        if(allowedIps == null || !allowedIps.contains(clientIp)) {
            // システムの問題じゃないからwarningログでする
            log.warn("Not Allowed IP requested. ip={}, uri={}", request.getRemoteAddr(), request.getRequestURI());
            handlerExceptionResolver.resolveException(request, response, null, new UnAuthorizedException(messageSource.getMessage("error.common.403", null, LocaleContextHolder.getLocale())));
            return;
        }

        filterChain.doFilter(request, response);
    }

}
