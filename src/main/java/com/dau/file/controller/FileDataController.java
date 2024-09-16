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

    @GetMapping
    public CommonResponseDto getFileData(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getFileData(standardTime));
    }

    @GetMapping(value = "/join-cnt")
    public CommonResponseDto getJoinCnt(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getJoinCnt(standardTime));
    }

    @GetMapping(value = "/quit-cnt")
    public CommonResponseDto getQuitCnt(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getQuitCnt(standardTime));
    }

    @GetMapping(value = "/payment-sum")
    public CommonResponseDto getPaymentSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getPaymentSum(standardTime));
    }

    @GetMapping(value = "/use-sum")
    public CommonResponseDto getUseSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getUseSum(standardTime));
    }

    @GetMapping(value = "/sales-sum")
    public CommonResponseDto getSalesSum(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.getSalesSum(standardTime));
    }

    @PatchMapping(value = "/join-cnt")
    public CommonResponseDto modifyJoinCnt(@Valid @RequestBody ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        return new CommonResponseDto(fileDataService.modifyJoinCnt(modifyUserCntChangeRequestDto));
    }

    @PatchMapping(value = "/quit-cnt")
    public CommonResponseDto modifyQuitCnt(@Valid @RequestBody ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        return new CommonResponseDto(fileDataService.modifyQuitCnt(modifyUserCntChangeRequestDto));
    }

    @PatchMapping(value = "/payment-sum")
    public CommonResponseDto modifyPaymentSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifyPaymentSum(modifySumRequestDto));
    }

    @PatchMapping(value = "/use-sum")
    public CommonResponseDto modifyUseSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifyUseSum(modifySumRequestDto));
    }

    @PatchMapping(value = "/sales-sum")
    public CommonResponseDto modifySalesSum(@Valid @RequestBody ModifySumRequestDto modifySumRequestDto) {
        return new CommonResponseDto(fileDataService.modifySalesSum(modifySumRequestDto));
    }

    @PutMapping
    public CommonResponseDto modifyFileData(@Valid @RequestBody ModifyFileDataRequestDto modifyFileDataRequestDto) {
        return new CommonResponseDto(fileDataService.modifyFileData(modifyFileDataRequestDto));
    }

    @DeleteMapping
    public CommonResponseDto deleteFileData(@RequestParam(value = "standardTime") LocalDateTime standardTime) {
        return new CommonResponseDto(fileDataService.deleteFileData(standardTime));
    }

}
