package com.dau.file.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommonResponseDto<T> {

    private T data;
    private MetaData meta;

    public CommonResponseDto(T data) {
        this.data = data;
    }

    public void setMeta(String version) {
        meta = new MetaData();
        meta.timestamp = LocalDateTime.now();
        meta.version = version;
    }

    @Data
    private static class MetaData {
        private LocalDateTime timestamp;
        private String version;
    }

}
