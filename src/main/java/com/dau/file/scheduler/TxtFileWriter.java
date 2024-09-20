package com.dau.file.scheduler;

import com.dau.file.entity.FileData;
import com.dau.file.exception.exception.FileWriteFailedException;
import com.dau.file.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TxtFileWriter implements FileWriter {

    private final MessageSource messageSource;
    private final FileDataRepository fileDataRepository;
    private static final String delimiter = "\\|";

    @Override
    public void writeFile(Path filePath) {
        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(delimiter);
                parts = Arrays.stream(parts).map(p -> p.replace(",", "")).toArray(String[]::new);
                LocalDateTime standardTime = LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                int joinCnt = Integer.parseInt(parts[1]);
                int quitCnt = Integer.parseInt(parts[2]);
                BigDecimal paymentSum = new BigDecimal(parts[3]);
                BigDecimal useSum = new BigDecimal(parts[4]);
                BigDecimal salesSum = new BigDecimal(parts[5]);

                FileData fileData = new FileData(standardTime, joinCnt, quitCnt, paymentSum, useSum, salesSum);
                fileDataRepository.save(fileData);
            }
        }catch(Exception e) {
            throw new FileWriteFailedException(messageSource.getMessage("error.file-write.failed", new String[]{filePath.toString(), e.getMessage()}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public boolean isMatchedExt(String fileName) {
        return fileName.toLowerCase().endsWith(".txt");
    }

}
