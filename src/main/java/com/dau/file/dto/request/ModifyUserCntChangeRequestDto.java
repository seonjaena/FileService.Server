package com.dau.file.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModifyUserCntChangeRequestDto {

    private LocalDateTime standardTime;
    private Integer value;

}
