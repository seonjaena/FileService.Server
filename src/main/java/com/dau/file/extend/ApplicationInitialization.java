package com.dau.file.extend;

import com.dau.file.entity.FileData;
import com.dau.file.entity.User;
import com.dau.file.entity.enums.STATUS;
import com.dau.file.repository.FileDataRepository;
import com.dau.file.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApplicationInitialization implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FileDataRepository fileDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User("test-user", passwordEncoder.encode("test-password"));
        userRepository.save(user);

        FileData fileData = new FileData(1L, LocalDateTime.now().withMinute(0).withSecond(0).withNano(0), 1, 1, new BigDecimal("10.1"), new BigDecimal("11.2"), new BigDecimal("12.3"), STATUS.NORMAL);
        fileDataRepository.save(fileData);
    }
}
