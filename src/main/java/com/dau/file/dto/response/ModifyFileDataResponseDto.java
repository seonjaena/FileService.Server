package com.dau.file.dto.response;

import com.dau.file.entity.FileData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModifyFileDataResponseDto {

    private LocalDateTime standardTime;
    private Integer joinCnt;
    private Integer quitCnt;
    private BigDecimal paymentSum;
    private BigDecimal useSum;
    private BigDecimal salesSum;

    public ModifyFileDataResponseDto(FileData fileData) {
        this.standardTime = fileData.getStandardTime();
        this.joinCnt = fileData.getJoinCnt();
        this.quitCnt = fileData.getQuitCnt();
        this.paymentSum = new BigDecimal(fileData.getPaymentSum().toString());
        this.useSum = new BigDecimal(fileData.getUseSum().toString());
        this.salesSum = new BigDecimal(fileData.getSalesSum().toString());
    }

}
