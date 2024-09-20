package com.dau.file.filter;

import com.dau.file.exception.exception.UnAuthorizedException;
import com.dau.file.extend.CidrUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpCheckFilter extends OncePerRequestFilter {

    @Value("${service.allowed-ips:#{null}}")
    private final List<String> allowedIps;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MessageSource messageSource;
    private List<String> allowedIpsCidr = new ArrayList<>();

    @PostConstruct
    public void init() {
        if(allowedIps != null) {
            for(String ip : allowedIps) {
                if(StringUtils.isBlank(ip)) {
                    continue;
                }
                allowedIpsCidr.add(ip.contains("/") ? ip : ip + "/32");
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        boolean isMatched = allowedIpsCidr.stream()
                .anyMatch(range -> CidrUtils.isInRange(range, clientIp));

        if(!isMatched) {
            Principal principal = request.getUserPrincipal();
            String userId = principal == null ? null : principal.getName();
            // 시스템에서 발생한 에러가 아니기 때문에 warning 레벨로 로그 출력
            log.warn("Not Allowed IP requested. ip={}, uri={}, userId={}", request.getRemoteAddr(), request.getRequestURI(), userId);
            handlerExceptionResolver.resolveException(request, response, null, new UnAuthorizedException(messageSource.getMessage("error.common.403", null, LocaleContextHolder.getLocale())));
            return;
        }

        filterChain.doFilter(request, response);
    }

}
