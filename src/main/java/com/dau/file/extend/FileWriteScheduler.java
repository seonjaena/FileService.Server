package com.dau.file.extend;

import com.dau.file.entity.FileData;
import com.dau.file.repository.FileDataRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileWriteScheduler {

    private final PlatformTransactionManager transactionManager;
    private final FileDataRepository fileDataRepository;

    @Value("${service.file-write-scheduler.file-directory}")
    private String schedulerFileDirectory;

    @Value("${service.file-write-scheduler.extension-delimiter}")
    private String extensionDelimiterStr;

    private Map<String, String> fileDelimiters = new HashMap<>();

    @PostConstruct
    public void init() {
        String[] extensionDelimiters = extensionDelimiterStr.split(" ");
        for(String extensionDelimiter : extensionDelimiters) {
            extensionDelimiter = extensionDelimiter.trim();

            String extension = extensionDelimiter.substring(0, extensionDelimiter.length() - 1);
            String delimiter = extensionDelimiter.substring(extensionDelimiter.length() - 1);

            if(StringUtils.isEmpty(extension) || StringUtils.isEmpty(delimiter)) {
                continue;
            }

            if(delimiter.equals("|")) {
                delimiter = delimiter.replace("|", "\\|");
            }

            this.fileDelimiters.put(extension, delimiter);
        }
    }

    @Scheduled(cron = "${service.file-write-scheduler.cron}")
    public void processFiles() {
        try {
            Files.walk(Paths.get(schedulerFileDirectory))
                    .filter(Files::isRegularFile)
                    .forEach(this::processFile);
        }catch(IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void processFile(Path filePath) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        String fileExtension = getFileExtension(filePath.toString());
        if(fileExtension == null) {
            return;
        }

        String delimiter = fileDelimiters.get(fileExtension);

        log.info("start writing file. filename = {}", filePath);

        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(delimiter);
                saveData(parts);
            }
            log.info("writing file is done. filename = {}", filePath);
            transactionManager.commit(transactionStatus);
        }catch(Exception e) {
            log.error("failed to write file. filename={}, origin error={}", filePath, e.getMessage());
            transactionManager.rollback(transactionStatus);
            return;
        }

        deleteFile(filePath);

    }

    private void saveData(String[] parts) {
        LocalDateTime standardTime = LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        int joinCnt = Integer.parseInt(parts[1]);
        int quitCnt = Integer.parseInt(parts[2]);
        BigDecimal paymentSum = new BigDecimal(parts[3].replace(",", "").toString());
        BigDecimal useSum = new BigDecimal(parts[4].replace(",", "").toString());
        BigDecimal salesSum = new BigDecimal(parts[5].replace(",", "").toString());

        FileData data = new FileData(standardTime, joinCnt, quitCnt, paymentSum, useSum, salesSum);
        fileDataRepository.saveAndFlush(data);
    }

    private void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
        }catch(Exception e) {
            log.error("failed to delete file. filename = {}", filePath);
        }
    }

    private String getFileExtension(String filename) {
        return !StringUtils.isEmpty(filename) && filename.contains(".") ? filename.substring(filename.lastIndexOf(".") + 1) : null;
    }

}
