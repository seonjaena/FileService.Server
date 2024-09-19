package com.dau.file.config;

import com.dau.file.jwt.JwtAuthenticationFilter;
import com.dau.file.exception.JwtAccessDeniedHandler;
import com.dau.file.exception.JwtAuthenticationEntryPoint;
import com.dau.file.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
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

    private final static String H2_CONSOLE_URL = "/h2-console/**";

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
                                .requestMatchers(HttpMethod.GET, PERMIT_ALL_METHOD_URL.get(HttpMethod.GET)).permitAll()
                                .requestMatchers(HttpMethod.POST, PERMIT_ALL_METHOD_URL.get(HttpMethod.POST)).permitAll()
                                .requestMatchers(HttpMethod.PUT, PERMIT_ALL_METHOD_URL.get(HttpMethod.PUT)).permitAll()
                                .requestMatchers(HttpMethod.PATCH, PERMIT_ALL_METHOD_URL.get(HttpMethod.PATCH)).permitAll()
                                .requestMatchers(HttpMethod.DELETE, PERMIT_ALL_METHOD_URL.get(HttpMethod.DELETE)).permitAll()
                                .requestMatchers(H2_CONSOLE_URL).permitAll()
                                .requestMatchers("/error").permitAll()
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
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
