package com.dau.file.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModifySumRequestDto {

    private LocalDateTime standardTime;
    private BigDecimal value;

}
