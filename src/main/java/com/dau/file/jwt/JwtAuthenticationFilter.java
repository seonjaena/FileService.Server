package com.dau.file.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private String h2ConsolePath;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, String h2ConsolePath) {
        this.jwtProvider = jwtProvider;
        this.h2ConsolePath = h2ConsolePath;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 만약 H2 콘솔에 대한 요청이라면 JWT 인증 제외
        return request.getRequestURI().startsWith(h2ConsolePath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtProvider.parseToken(request)
                .orElse("");

        // JWTに問題がなかったらSecurityContextHolderにユーザの情報をセッティングする
        if(StringUtils.hasText(token) && !jwtProvider.isTokenExpired(token)) {
            Authentication authentication = jwtProvider.getAuthUserInfo(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
