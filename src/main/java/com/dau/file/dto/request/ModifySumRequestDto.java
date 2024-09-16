package com.dau.file.dto.request;

import com.dau.file.validator.constraint.SumConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModifySumRequestDto {

    private LocalDateTime standardTime;

    @SumConstraint(min = "0")
    private BigDecimal value;

}
