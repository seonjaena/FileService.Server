package com.dau.file.service;

import com.dau.file.dto.request.ModifyFileDataRequestDto;
import com.dau.file.dto.request.ModifySumRequestDto;
import com.dau.file.dto.request.ModifyUserCntChangeRequestDto;
import com.dau.file.dto.response.ModifyFileDataResponseDto;
import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.RESULT;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.exception.exception.FileDataNotExistsException;
import com.dau.file.repository.FileDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FileDataService {

    private final FileDataRepository fileDataRepository;
    private final MessageSource messageSource;

    public Integer getJoinCnt(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        return fileData.getJoinCnt();
    }

    public Integer getQuitCnt(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        return fileData.getQuitCnt();
    }

    public BigDecimal getPaymentSum(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        return fileData.getPaymentSum();
    }

    public BigDecimal getUseSum(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        return fileData.getUseSum();
    }

    public BigDecimal getSalesSum(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        return fileData.getSalesSum();
    }

    public ModifyFileDataResponseDto modifyJoinCnt(ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        FileData fileData = getFileData(modifyUserCntChangeRequestDto.getStandardTime());
        fileData.modifyJoinCnt(modifyUserCntChangeRequestDto.getValue());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public ModifyFileDataResponseDto modifyQuitCnt(ModifyUserCntChangeRequestDto modifyUserCntChangeRequestDto) {
        FileData fileData = getFileData(modifyUserCntChangeRequestDto.getStandardTime());
        fileData.modifyQuitCnt(modifyUserCntChangeRequestDto.getValue());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public ModifyFileDataResponseDto modifyPaymentSum(ModifySumRequestDto modifySumRequestDto) {
        FileData fileData = getFileData(modifySumRequestDto.getStandardTime());
        fileData.modifyPaymentSum(modifySumRequestDto.getValue());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public ModifyFileDataResponseDto modifyUseSum(ModifySumRequestDto modifySumRequestDto) {
        FileData fileData = getFileData(modifySumRequestDto.getStandardTime());
        fileData.modifyUseSum(modifySumRequestDto.getValue());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public ModifyFileDataResponseDto modifySalesSum(ModifySumRequestDto modifySumRequestDto) {
        FileData fileData = getFileData(modifySumRequestDto.getStandardTime());
        fileData.modifySalesSum(modifySumRequestDto.getValue());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public ModifyFileDataResponseDto modifyFileData(ModifyFileDataRequestDto modifyFileDataRequestDto) {
        FileData fileData = getFileData(modifyFileDataRequestDto.getStandardTime());
        fileData.modifyJoinCnt(modifyFileDataRequestDto.getJoinCnt());
        fileData.modifyQuitCnt(modifyFileDataRequestDto.getQuitCnt());
        fileData.modifyPaymentSum(modifyFileDataRequestDto.getPaymentSum());
        fileData.modifyUseSum(modifyFileDataRequestDto.getUseSum());
        fileData.modifySalesSum(modifyFileDataRequestDto.getSalesSum());
        fileDataRepository.flush();
        return new ModifyFileDataResponseDto(fileData);
    }

    public RESULT deleteFileData(LocalDateTime standardTime) {
        FileData fileData = getFileData(standardTime);
        fileDataRepository.delete(fileData);
        return isFileDataExists(standardTime)? RESULT.FAIL : RESULT.SUCCESS;
    }

    public FileData getFileData(LocalDateTime standardTime) {
        return fileDataRepository.findByStandardTimeAndStatus(standardTime, STATUS.NORMAL)
                .orElseThrow(() -> new FileDataNotExistsException(messageSource.getMessage("error.file-data.not-exists", null, LocaleContextHolder.getLocale())));
    }

    private boolean isFileDataExists(LocalDateTime standardTime) {
        return fileDataRepository.findByStandardTimeAndStatus(standardTime, STATUS.NORMAL).isPresent();
    }

}
