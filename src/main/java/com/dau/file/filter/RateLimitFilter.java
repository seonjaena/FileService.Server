package com.dau.file.filter;

import com.dau.file.exception.exception.TooManyRequestsException;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private Bucket bucket;
    private final MessageSource messageSource;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("${service.rate-limit.init-bucket-size}")
    private long initBucketSize;

    @Value("${service.rate-limit.refill-token}")
    private long refillToken;

    @Value("${service.rate-limit.refill-second}")
    private long refillSecond;

    public RateLimitFilter(MessageSource messageSource, HandlerExceptionResolver handlerExceptionResolver) {
        this.messageSource = messageSource;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @PostConstruct
    public void init() {
        this.bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(initBucketSize).refillGreedy(refillToken, Duration.ofSeconds(refillSecond)))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }
        handlerExceptionResolver.resolveException(request, response, null, new TooManyRequestsException(messageSource.getMessage("error.common.429", null, LocaleContextHolder.getLocale())));
    }

}
