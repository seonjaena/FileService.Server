package com.dau.file.config;

import com.dau.file.jwt.JwtAuthenticationFilter;
import com.dau.file.exception.JwtAccessDeniedHandler;
import com.dau.file.exception.JwtAuthenticationEntryPoint;
import com.dau.file.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    private static final Map<HttpMethod, String[]> PERMIT_ALL_METHOD_URL = Map.ofEntries(
            Map.entry(HttpMethod.GET, new String[]{}),
            Map.entry(HttpMethod.POST, new String[]{"/auth"}),
            Map.entry(HttpMethod.PUT, new String[]{}),
            Map.entry(HttpMethod.PATCH, new String[]{}),
            Map.entry(HttpMethod.DELETE, new String[]{})
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http
                .cors((cors) ->
                        cors.configurationSource(corsConfigurationSource())
                )
                // Session을 사용하지 않기 때문에 CSRF 토큰은 필요 없음
                .csrf((csrf) ->
                        csrf.disable()
                )
                // H2 Console이 iframe 방식으로 화면 구성을 하는 것으로 보임. 따라서 iframe 보안 옵션을 disable (REST API 서버이기 때문에 disable 처리해도 무관)
                .headers((header) -> {
                    header.frameOptions(option -> {
                        option.disable();
                    });
                })
                // JWT를 사용하기 때문에 Session을 생성 및 사용하지 않는다.
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 기본: 모든 URL 요청은 ROLE을 가져야만 접근 가능하다.
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, PERMIT_ALL_METHOD_URL.get(HttpMethod.GET)).permitAll()
                                .requestMatchers(HttpMethod.POST, PERMIT_ALL_METHOD_URL.get(HttpMethod.POST)).permitAll()
                                .requestMatchers(HttpMethod.PUT, PERMIT_ALL_METHOD_URL.get(HttpMethod.PUT)).permitAll()
                                .requestMatchers(HttpMethod.PATCH, PERMIT_ALL_METHOD_URL.get(HttpMethod.PATCH)).permitAll()
                                .requestMatchers(HttpMethod.DELETE, PERMIT_ALL_METHOD_URL.get(HttpMethod.DELETE)).permitAll()
                                .requestMatchers(h2ConsolePath + "/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                )
                // REST API 형식의 서버이며 인증 정보를 JSON 형태로 받기 때문에 form login은 필요하지 않다.
                .formLogin((formLogin) ->
                        formLogin.disable()
                )
                // JWT를 사용하기 때문에 Http Basic 인증 방식은 사용하지 않는다.
                .httpBasic((httpBasic) ->
                        httpBasic.disable()
                )
                // Http 상태코드 401, 403에 대해서 처리하는 코드 등록
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
        ;

        // 요청 Header에서 JWT를 확인하는 필터 등록
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, h2ConsolePath), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // IP Check 필터가 존재하기 때문에 CORS 정책은 모두 허용하는 방향으로 개발한다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(false);   // CORS 정책이 '*' 이고 쿠키가 필요하지 않기 때문에 false로 설정한다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 로그인을 위해 해시화를 지원해야 한다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
