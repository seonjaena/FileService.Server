package com.dau.file.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ModifyFileDataRequestDto {

    private LocalDateTime standardTime;
    private Integer joinCnt;
    private Integer quitCnt;
    private BigDecimal paymentSum;
    private BigDecimal useSum;
    private BigDecimal salesSum;

}
