package com.dau.file.config;

import com.dau.file.security.IpCheckFilter;
import com.dau.file.security.JwtAuthenticationFilter;
import com.dau.file.exception.JwtAccessDeniedHandler;
import com.dau.file.exception.JwtAuthenticationEntryPoint;
import com.dau.file.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final IpConfig ipConfig;

    // application.ymlで定義したアクセスができるIP
    @Value("${service.allowed-ips}")
    private List<String> allowedIps;
    private static final String ALLOWED_IP_REGEXP = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private static final Pattern ALLOWED_IP_PATTERN = Pattern.compile(ALLOWED_IP_REGEXP);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        http
                .cors((cors) ->
                        cors.configurationSource(corsConfigurationSource())
                )
                // Sessionを使わないからcsrf tokenは必要じゃない
                .csrf((csrf) ->
                        csrf.disable()
                )
                .headers((header) -> {
                    header.frameOptions(option -> {
                        option.disable();
                    });
                })
                // JWTを使うからSessionはSTATELESSでする
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // どんなRequestでも認証が必要だ
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/auth", "/h2-console/**").permitAll()
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

        // ユーザのIPを確認するFilterを追加する
        http.addFilterBefore(new IpCheckFilter(ipConfig.getAllowedIps()), BasicAuthenticationFilter.class);
        // JWTを認証するFilterを追加する
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedIps = new ArrayList<>();
        for(String ip : allowedIps) {
            if(ALLOWED_IP_PATTERN.matcher(ip).matches()) {
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

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
