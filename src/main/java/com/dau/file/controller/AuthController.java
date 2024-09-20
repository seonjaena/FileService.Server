package com.dau.file.controller;

import com.dau.file.dto.CommonResponseDto;
import com.dau.file.dto.request.LoginRequestDto;
import com.dau.file.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public CommonResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return new CommonResponseDto(authService.login(loginRequestDto));
    }

}
