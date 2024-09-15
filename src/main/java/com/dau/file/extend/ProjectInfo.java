package com.dau.file.extend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectInfo {

    private final BuildProperties buildProperties;

    public String getVersion() {
        return buildProperties.getVersion();
    }

}
