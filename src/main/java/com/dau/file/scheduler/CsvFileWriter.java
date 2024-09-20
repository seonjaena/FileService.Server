package com.dau.file.scheduler;

import com.dau.file.entity.FileData;
import com.dau.file.exception.exception.FileWriteFailedException;
import com.dau.file.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvFileWriter implements FileWriter {

    private final FileDataRepository fileDataRepository;
    private final MessageSource messageSource;
    private static final String delimiter = ",";

    @Override
    public void writeFile(Path filePath) {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withDelimiter(delimiter.charAt(0))
                    .withQuote('"')
                    .withRecordSeparator("\n")
                    .withIgnoreEmptyLines(true)
                    .parse(reader);
            for (CSVRecord record : csvParser) {
                List<String> records = record.stream().map(r -> r.replace(",", "")).collect(Collectors.toList());
                LocalDateTime standardTime = LocalDateTime.parse(records.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                int joinCnt = Integer.parseInt(records.get(1));
                int quitCnt = Integer.parseInt(records.get(2));
                BigDecimal paymentSum = new BigDecimal(records.get(3));
                BigDecimal useSum = new BigDecimal(records.get(4));
                BigDecimal salesSum = new BigDecimal(records.get(5));

                FileData fileData = new FileData(standardTime, joinCnt, quitCnt, paymentSum, useSum, salesSum);
                fileDataRepository.save(fileData);
            }
        }catch(Exception e) {
            throw new FileWriteFailedException(messageSource.getMessage("error.file-write.failed", new String[]{filePath.toString(), e.getMessage()}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public boolean isMatchedExt(String fileName) {
        return fileName.toLowerCase().endsWith(".csv");
    }

}
