package com.dau.file.service;

import com.dau.file.dto.JwtDto;
import com.dau.file.dto.request.LoginRequestDto;
import com.dau.file.dto.response.LoginResponseDto;
import com.dau.file.entity.User;
import com.dau.file.exception.exception.UnAuthenticatedException;
import com.dau.file.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final JwtProvider jwtProvider;

    // 로그인을 하는 서비스 로직 (access token을 생성하고 사용자에게 리턴한다.)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User dbUser = userService.getUser(loginRequestDto.getUserId());

        if(!passwordEncoder.matches(loginRequestDto.getUserPw(), dbUser.getUserPw())) {
            log.warn("Password is incorrect. userId={}", loginRequestDto.getUserId());
            throw new UnAuthenticatedException(
                    messageSource.getMessage("error.user-id-pw.incorrect", null, LocaleContextHolder.getLocale())
            );
        }

        JwtDto jwt = jwtProvider.createToken(dbUser.getUserId(), List.of(dbUser.getRole()));
        return new LoginResponseDto(jwt.getAccessToken());
    }

}
