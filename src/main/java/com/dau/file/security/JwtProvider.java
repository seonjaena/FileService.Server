package com.dau.file.security;

import com.dau.file.dto.JwtDto;
import com.dau.file.exception.exception.UnAuthenticatedException;
import com.dau.file.exception.exception.UnAuthorizedException;
import com.dau.file.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @Value("${jwt.expire.refresh}")
    private Long refreshTokenExpireMilliSec;

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
     * AccessTokenとRefreshTokenを作る
     * @param userId
     * @param roles
     * @return
     */
    public JwtDto createToken(String userId, List<String> roles) {
        Date now = new Date();

        String accessToken = createAccessToken(userId, roles, now);
        String refreshToken = createRefreshToken(userId, roles, now);

        return new JwtDto(accessToken);
    }

    /**
     * AccessTokenを作る
     * @param userId
     * @param roles
     * @return
     */
    public String refreshAccessToken(String userId, List<String> roles) {
        return createAccessToken(userId, roles, new Date());
    }

    /**
     * JWTを使ってユーザを検証する (Authentication, Authorization)
     * @param token
     * @return
     */
    public Authentication getAuthUserInfo(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();

        // tokenにuserIdがあるかを確認する
        if(!StringUtils.hasText(userId)) {
            throw new UnAuthenticatedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }

        // tokenにユーザ権限があるかを確認
        if(claims.get(ROLES) == null) {
            throw new UnAuthorizedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }


        UserDetails authUser = userService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(authUser, "", authUser.getAuthorities());
    }

    /**
     * 要請のHeaderでJWTを得る
     * @param request
     * @return
     */
    public Optional<String> parseToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return Optional.of(authorization.substring(7));
        }
        return Optional.ofNullable(null);
    }

    /**
     * ユーザのJWTがまだ使えるかを確認する
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    /**
     * JWTでuserIdを得る
     * @param token
     * @return
     */
    public String getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * JWTで権限を得る
     * @param token
     * @return
     */
    public List<String> getUserRoles(String token) {
        Claims claims = parseClaims(token);
        return claims.get(ROLES, List.class);
    }

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

    private String createRefreshToken(String userId, List<String> roles, Date now) {
        return Jwts.builder()
                .setHeader(header)
                .subject(userId)
                .claim(ROLES, roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpireMilliSec))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parseClaims(String token) {
        if(!StringUtils.hasText(token)) {
            throw new UnAuthenticatedException(
                    messageSource.getMessage("notice.re-login.request", null, LocaleContextHolder.getLocale())
            );
        }
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
