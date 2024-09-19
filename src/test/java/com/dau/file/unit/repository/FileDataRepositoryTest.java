package com.dau.file.unit.repository;

import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.repository.FileDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class FileDataRepositoryTest {

    @Autowired
    private FileDataRepository fileDataRepository;

    private final int joinCnt = 10;
    private final int quitCnt = 20;
    private final BigDecimal paymentSum = new BigDecimal("10.1");
    private final BigDecimal useSum = new BigDecimal("11.2");
    private final BigDecimal salesSum = new BigDecimal("12.3");
    private final LocalDateTime fileTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
    private final STATUS status = STATUS.NORMAL;

    @BeforeEach
    void setUp() {
        FileData fileData = new FileData(1L, fileTime, joinCnt, quitCnt, paymentSum, useSum, salesSum, status);
        fileDataRepository.saveAndFlush(fileData);
    }

    @Test
    @DisplayName("날짜와 상태로 파일 데이터를 찾기")
    void findByStandardTimeAndStatus() {
        Optional<FileData> fileDataExist = fileDataRepository.findByStandardTimeAndStatus(fileTime, status);
        Optional<FileData> fileDataEmpty1 = fileDataRepository.findByStandardTimeAndStatus(fileTime.minusHours(1), status);
        Optional<FileData> fileDataEmpty2 = fileDataRepository.findByStandardTimeAndStatus(fileTime, STATUS.DELETED);

        assertThat(fileDataExist.isPresent()).isTrue();
        assertThat(fileDataEmpty1.isEmpty()).isTrue();
        assertThat(fileDataEmpty2.isEmpty()).isTrue();
    }

}
