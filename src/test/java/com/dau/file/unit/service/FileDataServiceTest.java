package com.dau.file.unit.service;

import com.dau.file.dto.request.ModifyFileDataRequestDto;
import com.dau.file.dto.request.ModifySumRequestDto;
import com.dau.file.dto.request.ModifyUserCntChangeRequestDto;
import com.dau.file.dto.response.ModifyFileDataResponseDto;
import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.RESULT;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.exception.exception.FileDataNotExistsException;
import com.dau.file.repository.FileDataRepository;
import com.dau.file.service.FileDataService;
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

    @Test
    @DisplayName(value = "공통 에러")
    void getFileDataException() {
        Throwable throwable = catchThrowable(() -> fileDataService.getFileData(fileTime));
        assertThat(throwable).isInstanceOf(FileDataNotExistsException.class);
    }

    @Test
    @DisplayName(value = "가입자 수 확인 - 에러 X")
    void getJoinCnt() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
        assertThat(fileDataService.getJoinCnt(fileTime)).isSameAs(joinCnt);
    }

    @Test
    @DisplayName(value = "탈퇴자 수 확인 - 에러 X")
    void getQuitCnt() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
        assertThat(fileDataService.getQuitCnt(fileTime)).isSameAs(quitCnt);
    }

    @Test
    @DisplayName(value = "결제금액 확인 - 에러 X")
    void getPaymentSum() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
        assertThat(fileDataService.getPaymentSum(fileTime)).isEqualTo(paymentSum);
    }

    @Test
    @DisplayName(value = "사용금액 확인 - 에러 X")
    void getUseSum() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
        assertThat(fileDataService.getUseSum(fileTime)).isEqualTo(useSum);
    }

    @Test
    @DisplayName(value = "매출금액 확인 - 에러 X")
    void getSalesSum() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));
        assertThat(fileDataService.getSalesSum(fileTime)).isEqualTo(salesSum);
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
    @DisplayName(value = "사용금액 변경 - 에러 X")
    void modifyUseSum() {
        BigDecimal updatedUseSum = useSum.add(BigDecimal.TEN);
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedUseSum);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto responseDto = fileDataService.modifyUseSum(requestDto);
        assertThat(fileData.getUseSum()).isEqualTo(updatedUseSum);
        assertThat(responseDto.getUseSum()).isEqualTo(updatedUseSum);
    }

    @Test
    @DisplayName(value = "매출금액 변경 - 에러 X")
    void modifySalesSum() {
        BigDecimal updatedSalesSum = salesSum.add(BigDecimal.TEN);
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedSalesSum);

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto responseDto = fileDataService.modifySalesSum(requestDto);
        assertThat(fileData.getSalesSum()).isEqualTo(updatedSalesSum);
        assertThat(responseDto.getSalesSum()).isEqualTo(updatedSalesSum);
    }

    @Test
    @DisplayName(value = "파일 데이터 전체 변경 - 에러 X")
    void modifyFileData() {
        ModifyFileDataRequestDto modifyFileDataRequestDto = new ModifyFileDataRequestDto();
        modifyFileDataRequestDto.setStandardTime(fileTime);
        modifyFileDataRequestDto.setJoinCnt(joinCnt + 10);
        modifyFileDataRequestDto.setQuitCnt(quitCnt + 10);
        modifyFileDataRequestDto.setPaymentSum(paymentSum.add(BigDecimal.TEN));
        modifyFileDataRequestDto.setUseSum(useSum.add(BigDecimal.TEN));
        modifyFileDataRequestDto.setSalesSum(salesSum.add(BigDecimal.TEN));

        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData));

        ModifyFileDataResponseDto modifyFileDataResponseDto = fileDataService.modifyFileData(modifyFileDataRequestDto);

        assertThat(fileData.getJoinCnt()).isSameAs(joinCnt + 10);
        assertThat(fileData.getQuitCnt()).isSameAs(quitCnt + 10);
        assertThat(fileData.getPaymentSum()).isEqualTo(paymentSum.add(BigDecimal.TEN));
        assertThat(fileData.getUseSum()).isEqualTo(useSum.add(BigDecimal.TEN));
        assertThat(fileData.getSalesSum()).isEqualTo(salesSum.add(BigDecimal.TEN));

        assertThat(modifyFileDataResponseDto.getJoinCnt()).isSameAs(joinCnt + 10);
        assertThat(modifyFileDataResponseDto.getQuitCnt()).isSameAs(quitCnt + 10);
        assertThat(modifyFileDataResponseDto.getPaymentSum()).isEqualTo(paymentSum.add(BigDecimal.TEN));
        assertThat(modifyFileDataResponseDto.getUseSum()).isEqualTo(useSum.add(BigDecimal.TEN));
        assertThat(modifyFileDataResponseDto.getSalesSum()).isEqualTo(salesSum.add(BigDecimal.TEN));
    }

    @Test
    @DisplayName(value = "파일 데이터 삭제 - 에러 X")
    void deleteFileData() {
        when(fileDataRepository.findByStandardTimeAndStatus(fileTime, status)).thenReturn(Optional.of(fileData)).thenReturn(Optional.empty());
        RESULT result = fileDataService.deleteFileData(fileTime);

        assertThat(fileData.getStatus()).isSameAs(STATUS.DELETED);
        assertThat(result).isSameAs(RESULT.SUCCESS);
    }

}
