package com.dau.file.extend;

import com.dau.file.entity.User;
import com.dau.file.repository.FileDataRepository;
import com.dau.file.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FileDataRepository fileDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${service.init-user.id}")
    private String initUserId;
    @Value("${service.init-user.pw}")
    private String initUserPw;

    // 스프링 빈들이 로드되면 자동으로 필요한 데이터를 DB에 넣는등의 초기화 작업을 진행한다.
    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User(initUserId, passwordEncoder.encode(initUserPw));
        userRepository.save(user);
    }
}
