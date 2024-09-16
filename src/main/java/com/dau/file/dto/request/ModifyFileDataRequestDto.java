package com.dau.file.dto.request;

import com.dau.file.validator.constraint.SumConstraint;
import com.dau.file.validator.constraint.UserCntConstraint;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ModifyFileDataRequestDto {

    private LocalDateTime standardTime;
    @UserCntConstraint(min = 0)
    private Integer joinCnt;
    @UserCntConstraint(min = 0)
    private Integer quitCnt;
    @SumConstraint(min = "0")
    private BigDecimal paymentSum;
    @SumConstraint(min = "0")
    private BigDecimal useSum;
    @SumConstraint(min = "0")
    private BigDecimal salesSum;

}
