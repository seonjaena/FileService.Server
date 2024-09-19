package com.dau.file.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {

    private String userId;
    private String userPw;

}
