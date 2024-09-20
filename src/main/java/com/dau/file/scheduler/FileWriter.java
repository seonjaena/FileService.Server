package com.dau.file.scheduler;

import java.nio.file.Path;

public interface FileWriter {
    void writeFile(Path filePath);
    boolean isMatchedExt(String fileName);
}
