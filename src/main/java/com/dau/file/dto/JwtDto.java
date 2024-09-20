package com.dau.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {

    // TODO: refresh token에 대한 내용이 추가될 경우 refreshToken 필드도 추가 필요
    private String accessToken;

}
