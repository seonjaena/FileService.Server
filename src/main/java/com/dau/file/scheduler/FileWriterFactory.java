package com.dau.file.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FileWriterFactory {

    private final List<FileWriter> writers;

    public Optional<FileWriter> getWriter(String fileName) {
        return writers.stream()
                .filter(writer -> writer.isMatchedExt(fileName))
                .findFirst();
    }

}
