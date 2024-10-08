package com.dau.file.jwt;

import com.dau.file.dto.JwtDto;
import com.dau.file.exception.exception.UnAuthenticatedException;
import com.dau.file.exception.exception.UnAuthorizedException;
import com.dau.file.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtProvider {

    private final Key key;
    private final Header header;
    private final UserService userService;
    private final MessageSource messageSource;
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer";
    private final static String ROLES = "roles";

    @Value("${jwt.expire.access}")
    private Long accessTokenExpireMilliSec;

    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserService userService, MessageSource messageSource) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        Jwts.HeaderBuilder header = Jwts.header();
        header.setType("JWT");
        this.header = header.build();
        this.userService = userService;
        this.messageSource = messageSource;
    }

    /**
     * JWT 생성
     * @param userId
     * @param roles
     * @return
     */
    public JwtDto createToken(String userId, List<String> roles) {
        Date now = new Date();

        String accessToken = createAccessToken(userId, roles, now);

        return new JwtDto(accessToken);
    }

    /**
     * JWT에서 사용자 정보 파싱 (Authentication, Authorization)
     * @param token
     * @return
     */
    public Authentication getAuthUserInfo(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();

        // 사용자 계정
        if(!StringUtils.hasText(userId)) {
            throw new UnAuthenticatedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }

        // 사용자 권한 검사
        if(claims.get(ROLES) == null) {
            throw new UnAuthorizedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }


        UserDetails authUser = userService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(authUser, "", authUser.getAuthorities());
    }

    /**
     * 요청 헤더에서 JWT를 얻는다.
     * @param request
     * @return
     */
    public Optional<String> parseToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return Optional.of(authorization.substring(BEARER_PREFIX.length() + 1));
        }
        return Optional.ofNullable(null);
    }

    /**
     * JWT의 만료 여부를 검사한다.
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Access Token 생성 (TODO: 추후 필요하다면 refresh token을 생성하는 함수도 필요함)
     * @param userId
     * @param roles
     * @param now
     * @return
     */
    private String createAccessToken(String userId, List<String> roles, Date now) {
        return Jwts.builder()
                .setHeader(header)
                .subject(userId)
                .claim(ROLES, roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpireMilliSec))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parseClaims(String token) {
        if(!StringUtils.hasText(token)) {
            throw new UnAuthenticatedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
