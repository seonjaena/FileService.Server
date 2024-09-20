package com.dau.file.controller;

import com.dau.file.dto.CommonResponseDto;
import com.dau.file.dto.request.ModifyFileDataRequestDto;
import com.dau.file.dto.request.ModifySumRequestDto;
import com.dau.file.dto.request.ModifyUserCntChangeRequestDto;
import com.dau.file.service.FileDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/file-data")
@RequiredArgsConstructor
public class FileDataController {

    private final FileDataService fileDataService;

    // 데이터 등록 날짜로 파일의 데이터 조회
    @GetMapping
    public CommonResponseDto getFileData(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getFileData(standardTime));
    }

    // 데이터 등록 날짜로 가입자 수 조회
    @GetMapping(value = "/join-cnt")
    public CommonResponseDto getJoinCnt(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getJoinCnt(standardTime));
    }

    // 데이터 등록 날짜로 탈퇴자 수 조회
    @GetMapping(value = "/quit-cnt")
    public CommonResponseDto getQuitCnt(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getQuitCnt(standardTime));
    }

    // 데이터 등록 날짜로 결제금액 조회
    @GetMapping(value = "/payment-sum")
    public CommonResponseDto getPaymentSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getPaymentSum(standardTime));
    }

    // 데이터 등록 날짜로 사용금액 조회
    @GetMapping(value = "/use-sum")
    public CommonResponseDto getUseSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getUseSum(standardTime));
    }

    // 데이터 등록 날짜로 매출 금액 조회
    @GetMapping(value = "/sales-sum")
    public CommonResponseDto getSalesSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getSalesSum(standardTime));
    }

    // 가입자 수 수정
    @PatchMapping(value = "/join-cnt")
    public CommonResponseDto modifyJoinCnt(@Valid @RequestBody ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        return new CommonResponseDto(fileDataService.modifyJoinCnt(modifyUserCntChangeRequestDto));
    }

    // 탈퇴자 수 수정
    @PatchMapping(value = "/quit-cnt")
    public CommonResponseDto modifyQuitCnt(@Valid @RequestBody ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        return new CommonResponseDto(fileDataService.modifyQuitCnt(modifyUserCntChangeRequestDto));
    }

    // 결제금액 수정
    @PatchMapping(value = "/payment-sum")
    public CommonResponseDto modifyPaymentSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifyPaymentSum(modifySumRequestDto));
    }

    // 사용금액 수정
    @PatchMapping(value = "/use-sum")
    public CommonResponseDto modifyUseSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifyUseSum(modifySumRequestDto));
    }

    // 판매금액 수정
    @PatchMapping(value = "/sales-sum")
    public CommonResponseDto modifySalesSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifySalesSum(modifySumRequestDto));
    }

    // 파일 데이터 수정
    @PutMapping
    public CommonResponseDto modifyFileData(@Valid @RequestBody ModifyFileDataRequestDto modifyFileDataRequestDto) {
        return new CommonResponseDto(fileDataService.modifyFileData(modifyFileDataRequestDto));
    }

    // 파일 데이터 삭제
    @DeleteMapping
    public CommonResponseDto deleteFileData(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.deleteFileData(standardTime));
    }

}
