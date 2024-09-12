package com.dau.file.config;

import com.dau.file.auth.JwtAuthenticationFilter;
import com.dau.file.exception.JwtAccessDeniedHandler;
import com.dau.file.exception.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    // application.ymlで定義したアクセスができるIP
    @Value("${service.enable-ips}")
    private List<String> enableIps;
    private static final String ENABLE_IP_REGEXP = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private static final Pattern ENABLE_IP_PATTERN = Pattern.compile(ENABLE_IP_REGEXP);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) ->
                        cors.configurationSource(corsConfigurationSource())
                )
                // Sessionを使わないからcsrf tokenは必要じゃない
                .csrf((csrf) ->
                        csrf.disable()
                )
                // JWTを使うからSessionはSTATELESSでする
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // どんなRequestでも認証が必要だ
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                // JWTを使うからFormLoginは使わない
                .formLogin((formLogin) ->
                        formLogin.disable()
                )
                // 基本的に提供されるLogin Formは使わない
                .httpBasic((httpBasic) ->
                        httpBasic.disable()
                )
                .exceptionHandling((exceptionHandler) ->
                        exceptionHandler
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
        ;

        // JWTを認証するFilterを追加する
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedIps = new ArrayList<>();
        for(String ip : enableIps) {
            if(ENABLE_IP_PATTERN.matcher(ip).matches()) {
                allowedIps.add("http://" + ip);
                allowedIps.add("https://" + ip);
            }
        }

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(allowedIps);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
