package com.dau.file.dto.request;

import com.dau.file.validator.constraint.UserCntConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ModifyUserCntChangeRequestDto {

    private LocalDateTime standardTime;

    @UserCntConstraint(min = 0)
    private Integer value;

}
