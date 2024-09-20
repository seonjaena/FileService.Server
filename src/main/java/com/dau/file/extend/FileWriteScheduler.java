package com.dau.file.extend;

import com.dau.file.scheduler.FileWriter;
import com.dau.file.scheduler.FileWriterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileWriteScheduler {

    private final PlatformTransactionManager transactionManager;
    private final FileWriterFactory fileWriterFactory;

    @Value("${service.file-write-scheduler.file-directory}")
    private String schedulerFileDirectory;

    @Scheduled(cron = "${service.file-write-scheduler.cron}")
    public void processFiles() {
        try(Stream<Path> paths = Files.walk(Paths.get(schedulerFileDirectory))) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::processFile);
        }catch(IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void processFile(Path filePath) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {
            String fileName = filePath.getFileName().toString();
            Optional<FileWriter> writer = fileWriterFactory.getWriter(fileName);
            if(writer.isEmpty()) {
                return;
            }
            log.info("start writing file. filename = {}", filePath);
            writer.get().writeFile(filePath);
            transactionManager.commit(transactionStatus);
            log.info("writing file is done. filename = {}", filePath);
        }catch(Exception e) {
            transactionManager.rollback(transactionStatus);
            log.error("failed to write file. filename={}, origin error={}", filePath, e.getMessage());
            return;
        }

        deleteFile(filePath);

    }

    private void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
        }catch(Exception e) {
            log.error("failed to delete file. filename = {}", filePath);
        }
    }

}
