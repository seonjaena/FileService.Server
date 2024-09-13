package com.dau.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IpConfig {

    @Value("${service.allowed-ips:#{null}}")
    private List<String> allowedIps;

    public List<String> getAllowedIps() {
        return allowedIps;
    }

    private void setAllowedIps(List<String> allowedIps) {
        this.allowedIps = allowedIps;
    }

}
