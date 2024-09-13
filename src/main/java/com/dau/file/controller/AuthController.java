package com.dau.file.controller;

import com.dau.file.dto.request.LoginRequestDto;
import com.dau.file.dto.response.LoginResponseDto;
import com.dau.file.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) throws NoSuchAlgorithmException {
        return authService.login(loginRequestDto);
    }

    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

}
