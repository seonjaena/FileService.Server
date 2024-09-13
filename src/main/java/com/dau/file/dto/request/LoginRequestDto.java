package com.dau.file.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginRequestDto {

    private String userId;
    private String userPw;

}
