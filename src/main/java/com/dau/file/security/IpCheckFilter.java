package com.dau.file.security;

import com.dau.file.exception.exception.UnAuthorizedException;
import com.dau.file.extend.ApplicationContextProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.List;

@Slf4j
public class IpCheckFilter extends OncePerRequestFilter {

    private final List<String> allowedIps;
    private HandlerExceptionResolver handlerExceptionResolver;
    private MessageSource messageSource;

    public IpCheckFilter(List<String> allowedIps) {
        this.allowedIps = allowedIps;
        this.handlerExceptionResolver = ApplicationContextProvider.getApplicationContext().getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
        this.messageSource = ApplicationContextProvider.getApplicationContext().getBean(MessageSource.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();

        if(!allowedIps.contains(clientIp)) {
            // システムの問題じゃないからwarningログでする
            log.warn("Not Allowed IP requested. ip={}, uri={}", request.getRemoteAddr(), request.getRequestURI());
            handlerExceptionResolver.resolveException(request, response, null, new UnAuthorizedException(messageSource.getMessage("error.common.403", null, LocaleContextHolder.getLocale())));
            return;
        }

        filterChain.doFilter(request, response);
    }

}
