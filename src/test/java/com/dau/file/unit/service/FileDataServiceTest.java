package com.dau.file.unit.service;

import com.dau.file.dto.request.ModifySumRequestDto;
import com.dau.file.dto.request.ModifyUserCntChangeRequestDto;
import com.dau.file.dto.response.ModifyFileDataResponseDto;
import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.exception.exception.FileDataNotExistsException;
import com.dau.file.repository.FileDataRepository;
import com.dau.file.service.FileDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileDataServiceTest {

    @Mock
    private FileDataRepository fileDataRepository;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FileDataService fileDataService;

    private final int joinCnt = 10;
    private final int quitCnt = 20;
    private final BigDecimal paymentSum = new BigDecimal("10.1");
    private final BigDecimal useSum = new BigDecimal("11.2");
    private final BigDecimal salesSum = new BigDecimal("12.3");
    private final LocalDateTime fileTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
    private final STATUS status = STATUS.NORMAL;
    private FileData fileData;

    @BeforeEach
    void setUp() {
        this.fileData = new FileData(1L, fileTime, joinCnt, quitCnt, paymentSum, useSum, salesSum, status);
    }

//    @Test
//    @DisplayName(value = "가입자 수 확인 - 에러 X")
//    void getJoinCnt() {
//        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
//        assertThat(fileDataService.getJoinCnt(fileTime))
//                .isSameAs(joinCnt);
//    }

    @Test
    @DisplayName(value = "가입자 수 확인 - 에러 O")
    void getJoinCntException() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> fileDataService.getJoinCnt(fileTime));
        assertThat(throwable)
                .isInstanceOf(FileDataNotExistsException.class);
    }

//    @Test
//    @DisplayName(value = "탈퇴자 수 확인 - 에러 X")
//    void getQuitCnt() {
//        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
//        assertThat(fileDataService.getQuitCnt(fileTime))
//                .isSameAs(quitCnt);
//    }

    @Test
    @DisplayName(value = "탈퇴자 수 확인 - 에러 O")
    void getQuitCntException() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> fileDataService.getQuitCnt(fileTime));
        assertThat(throwable)
                .isInstanceOf(FileDataNotExistsException.class);
    }

//    @Test
//    @DisplayName(value = "결제금액 확인 - 에러 X")
//    void getPaymentSum() {
//        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
//        assertThat(fileDataService.getPaymentSum(fileTime))
//                .isSameAs(paymentSum);
//    }

    @Test
    @DisplayName(value = "결제금액 확인 - 에러 O")
    void getPaymentSumException() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> fileDataService.getPaymentSum(fileTime));
        assertThat(throwable)
                .isInstanceOf(FileDataNotExistsException.class);
    }

//    @Test
//    @DisplayName(value = "사용금액 확인 - 에러 X")
//    void getUseSum() {
//        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
//        assertThat(fileDataService.getUseSum(fileTime))
//                .isSameAs(useSum);
//    }

    @Test
    @DisplayName(value = "사용금액 확인 - 에러 O")
    void getUseSumException() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> fileDataService.getUseSum(fileTime));
        assertThat(throwable)
                .isInstanceOf(FileDataNotExistsException.class);
    }

//    @Test
//    @DisplayName(value = "매출금액 확인 - 에러 X")
//    void getSalesSum() {
//        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
//        assertThat(fileDataService.getSalesSum(fileTime))
//                .isSameAs(salesSum);
//    }

    @Test
    @DisplayName(value = "매출금액 확인 - 에러 O")
    void getSalesSumException() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> fileDataService.getSalesSum(fileTime));
        assertThat(throwable)
                .isInstanceOf(FileDataNotExistsException.class);
    }

    @Test
    @DisplayName(value = "가입자 수 변경 - 에러 X")
    void modifyJoinCnt() {
        int updatedJoinCnt = joinCnt + 10;
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, updatedJoinCnt);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto responseDto = fileDataService.modifyJoinCnt(requestDto);
        assertThat(fileData.getJoinCnt()).isSameAs(updatedJoinCnt);
        assertThat(responseDto.getJoinCnt()).isSameAs(updatedJoinCnt);
    }

    @Test
    @DisplayName(value = "가입자 수 변경 - 에러 O")
    void modifyJoinCntException() {
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, joinCnt + 10);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> fileDataService.modifyJoinCnt(requestDto));
        assertThat(throwable).isInstanceOf(FileDataNotExistsException.class);
    }

    @Test
    @DisplayName(value = "탈퇴자 수 변경 - 에러 X")
    void modifyQuitCnt() {
        int updatedQuitCnt = quitCnt + 10;
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, updatedQuitCnt);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto responseDto = fileDataService.modifyQuitCnt(requestDto);
        assertThat(fileData.getQuitCnt()).isSameAs(updatedQuitCnt);
        assertThat(responseDto.getQuitCnt()).isSameAs(updatedQuitCnt);
    }

    @Test
    @DisplayName(value = "탈퇴자 수 변경 - 에러 O")
    void modifyQuitCntException() {
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, quitCnt + 10);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> fileDataService.modifyQuitCnt(requestDto));
        assertThat(throwable).isInstanceOf(FileDataNotExistsException.class);
    }

    @Test
    @DisplayName(value = "결제금액 변경 - 에러 X")
    void modifyPaymentSum() {
        BigDecimal updatedPaymentSum = paymentSum.add(BigDecimal.TEN);
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedPaymentSum);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto responseDto = fileDataService.modifyPaymentSum(requestDto);
        assertThat(fileData.getPaymentSum()).isEqualTo(updatedPaymentSum);
        assertThat(responseDto.getPaymentSum()).isEqualTo(updatedPaymentSum);
    }

    @Test
    @DisplayName(value = "결제금액 변경 - 에러 O")
    void modifyPaymentSumException() {
        BigDecimal updatedPaymentSum = paymentSum.add(BigDecimal.TEN);
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedPaymentSum);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> fileDataService.modifyPaymentSum(requestDto));
        assertThat(throwable).isInstanceOf(FileDataNotExistsException.class);
    }

}
