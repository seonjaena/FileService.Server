package com.dau.file.unit.controller;

import com.dau.file.controller.FileDataController;
import com.dau.file.dto.request.ModifyFileDataRequestDto;
import com.dau.file.dto.request.ModifySumRequestDto;
import com.dau.file.dto.request.ModifyUserCntChangeRequestDto;
import com.dau.file.dto.response.ModifyFileDataResponseDto;
import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.RESULT;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.service.FileDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FileDataController.class, properties = "spring.config.location=classpath:/application-test.yml")
@ActiveProfiles(value = {"test"})
public class FileDataControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileDataService fileDataService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final int joinCnt = 10;
    private final int quitCnt = 20;
    private final BigDecimal paymentSum = new BigDecimal("10.1");
    private final BigDecimal useSum = new BigDecimal("11.2");
    private final BigDecimal salesSum = new BigDecimal("12.3");
    private final LocalDateTime fileTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
    private final STATUS status = STATUS.NORMAL;
    private FileData fileData;

    private final int updatedJoinCnt = joinCnt + 10;
    private final int updatedQuitCnt = quitCnt + 10;
    private final BigDecimal updatedPaymentSum = paymentSum.add(BigDecimal.TEN);
    private final BigDecimal updatedUseSum = useSum.add(BigDecimal.TEN);
    private final BigDecimal updatedSalesSum = salesSum.add(BigDecimal.TEN);

    @BeforeEach
    void setUp() {
        this.fileData = new FileData(1L, fileTime, joinCnt, quitCnt, paymentSum, useSum, salesSum, status);
    }

    @Test
    @DisplayName(value = "파일 데이터 확인")
    @WithMockUser
    void getFileData() throws Exception {

        when(fileDataService.getFileData(fileTime)).thenReturn(fileData);

        mockMvc.perform(get("/file-data")
                        .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(fileData.getJoinCnt()))
                .andExpect(jsonPath("$.data.quitCnt").value(fileData.getQuitCnt()))
                .andExpect(jsonPath("$.data.paymentSum").value(fileData.getPaymentSum()))
                .andExpect(jsonPath("$.data.useSum").value(fileData.getUseSum()))
                .andExpect(jsonPath("$.data.salesSum").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "가입자 수 확인")
    @WithMockUser
    void getJoinCnt() throws Exception {
        when(fileDataService.getJoinCnt(fileTime)).thenReturn(fileData.getJoinCnt());

        mockMvc.perform(get("/file-data/join-cnt")
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(fileData.getJoinCnt()));;
    }

    @Test
    @DisplayName(value = "탈퇴자 수 확인")
    @WithMockUser
    void getQuitCnt() throws Exception {
        when(fileDataService.getQuitCnt(fileTime)).thenReturn(fileData.getQuitCnt());

        mockMvc.perform(get("/file-data/quit-cnt")
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(fileData.getQuitCnt()));;
    }

    @Test
    @DisplayName(value = "결제금액 확인")
    @WithMockUser
    void getPaymentSum() throws Exception {
        when(fileDataService.getPaymentSum(fileTime)).thenReturn(fileData.getPaymentSum());

        mockMvc.perform(get("/file-data/payment-sum")
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(fileData.getPaymentSum()));
    }

    @Test
    @DisplayName(value = "사용금액 확인")
    @WithMockUser
    void getUseSum() throws Exception {
        when(fileDataService.getUseSum(fileTime)).thenReturn(fileData.getUseSum());

        mockMvc.perform(get("/file-data/use-sum")
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(fileData.getUseSum()));
    }

    @Test
    @DisplayName(value = "매출금액 확인")
    @WithMockUser
    void getSalesSum() throws Exception {
        when(fileDataService.getSalesSum(fileTime)).thenReturn(fileData.getSalesSum());

        mockMvc.perform(get("/file-data/sales-sum")
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "가입자 수 변경")
    @WithMockUser
    void modifyJoinCnt() throws Exception {
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, updatedJoinCnt);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, updatedJoinCnt, quitCnt, paymentSum, useSum, salesSum);


        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifyJoinCnt(requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/file-data/join-cnt")
                .with(csrf())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(updatedJoinCnt))
                .andExpect(jsonPath("$.data.quitCnt").value(fileData.getQuitCnt()))
                .andExpect(jsonPath("$.data.paymentSum").value(fileData.getPaymentSum()))
                .andExpect(jsonPath("$.data.useSum").value(fileData.getUseSum()))
                .andExpect(jsonPath("$.data.salesSum").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "탈퇴자 수 변경")
    @WithMockUser
    void modifyQuitCnt() throws Exception {
        ModifyUserCntChangeRequestDto requestDto = new ModifyUserCntChangeRequestDto(fileTime, updatedQuitCnt);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, joinCnt, updatedQuitCnt, paymentSum, useSum, salesSum);

        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifyQuitCnt(requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/file-data/quit-cnt")
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(fileData.getJoinCnt()))
                .andExpect(jsonPath("$.data.quitCnt").value(updatedQuitCnt))
                .andExpect(jsonPath("$.data.paymentSum").value(fileData.getPaymentSum()))
                .andExpect(jsonPath("$.data.useSum").value(fileData.getUseSum()))
                .andExpect(jsonPath("$.data.salesSum").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "결제금액 변경")
    @WithMockUser
    void modifyPaymentSum() throws Exception {
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedPaymentSum);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, joinCnt, quitCnt, updatedPaymentSum, useSum, salesSum);

        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifyPaymentSum(requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/file-data/payment-sum")
                .with(csrf())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(fileData.getJoinCnt()))
                .andExpect(jsonPath("$.data.quitCnt").value(fileData.getQuitCnt()))
                .andExpect(jsonPath("$.data.paymentSum").value(updatedPaymentSum))
                .andExpect(jsonPath("$.data.useSum").value(fileData.getUseSum()))
                .andExpect(jsonPath("$.data.salesSum").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "사용금액 변경")
    @WithMockUser
    void modifyUseSum() throws Exception {
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedUseSum);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, joinCnt, quitCnt, paymentSum, updatedUseSum, salesSum);

        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifyUseSum(requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/file-data/use-sum")
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(fileData.getJoinCnt()))
                .andExpect(jsonPath("$.data.quitCnt").value(fileData.getQuitCnt()))
                .andExpect(jsonPath("$.data.paymentSum").value(fileData.getPaymentSum()))
                .andExpect(jsonPath("$.data.useSum").value(updatedUseSum))
                .andExpect(jsonPath("$.data.salesSum").value(fileData.getSalesSum()));
    }

    @Test
    @DisplayName(value = "매출금액 변경")
    @WithMockUser
    void modifySalesSum() throws Exception {
        ModifySumRequestDto requestDto = new ModifySumRequestDto(fileTime, updatedSalesSum);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, joinCnt, quitCnt, paymentSum, useSum, updatedSalesSum);

        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifySalesSum(requestDto)).thenReturn(responseDto);

        mockMvc.perform(patch("/file-data/sales-sum")
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(fileData.getJoinCnt()))
                .andExpect(jsonPath("$.data.quitCnt").value(fileData.getQuitCnt()))
                .andExpect(jsonPath("$.data.paymentSum").value(fileData.getPaymentSum()))
                .andExpect(jsonPath("$.data.useSum").value(useSum))
                .andExpect(jsonPath("$.data.salesSum").value(updatedSalesSum));
    }

    @Test
    @DisplayName(value = "파일 데이터 변경 - 에러 X")
    @WithMockUser
    void modifyFileData() throws Exception {
        ModifyFileDataRequestDto requestDto = new ModifyFileDataRequestDto(fileTime, updatedJoinCnt, updatedQuitCnt, updatedPaymentSum, updatedUseSum, updatedSalesSum);
        ModifyFileDataResponseDto responseDto = new ModifyFileDataResponseDto(fileTime, updatedJoinCnt, updatedQuitCnt, updatedPaymentSum, updatedUseSum, updatedSalesSum);

        String content = objectMapper.writeValueAsString(requestDto);

        when(fileDataService.modifyFileData(requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/file-data")
                .with(csrf())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.standardTime").value(formatter.format(fileData.getStandardTime())))
                .andExpect(jsonPath("$.data.joinCnt").value(updatedJoinCnt))
                .andExpect(jsonPath("$.data.quitCnt").value(updatedQuitCnt))
                .andExpect(jsonPath("$.data.paymentSum").value(updatedPaymentSum))
                .andExpect(jsonPath("$.data.useSum").value(updatedUseSum))
                .andExpect(jsonPath("$.data.salesSum").value(updatedSalesSum));
    }

    @Test
    @DisplayName(value = "파일 데이터 변경 - 에러 O")
    @WithMockUser
    void modifyFileDataException() throws Exception {
        List<ModifyFileDataRequestDto> requestDtoList = List.of(
                new ModifyFileDataRequestDto(fileTime, -1, quitCnt, paymentSum, useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, Integer.MAX_VALUE + 1, quitCnt, paymentSum, useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, -1, paymentSum, useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, Integer.MAX_VALUE + 1, paymentSum, useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, new BigDecimal(-1), useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, new BigDecimal(Integer.MAX_VALUE + 1), useSum, salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, paymentSum, new BigDecimal(-1), salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, paymentSum, new BigDecimal(Integer.MAX_VALUE + 1), salesSum),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, paymentSum, useSum, new BigDecimal(-1)),
                new ModifyFileDataRequestDto(fileTime, joinCnt, quitCnt, paymentSum, useSum, new BigDecimal(Integer.MAX_VALUE + 1))
        );

        for(ModifyFileDataRequestDto requestDto : requestDtoList) {
            mockMvc.perform(put("/file-data")
                            .with(csrf())
                            .content(objectMapper.writeValueAsString(requestDto))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName(value = "파일 삭제")
    @WithMockUser
    void deleteFileData() throws Exception {
        when(fileDataService.deleteFileData(fileTime)).thenReturn(RESULT.SUCCESS);

        mockMvc.perform(delete("/file-data")
                .with(csrf())
                .param("standardTime", fileTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(RESULT.SUCCESS.name()));
    }

}
