package com.dau.file.config;

import com.dau.file.filter.RateLimitFilter;
import com.dau.file.filter.IpCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final RateLimitFilter rateLimitFilter;
    private final IpCheckFilter ipCheckFilter;

    @Bean
    public FilterRegistrationBean<RateLimitFilter> globalRateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<IpCheckFilter> globalIpCheckFilter() {
        FilterRegistrationBean<IpCheckFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(ipCheckFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}