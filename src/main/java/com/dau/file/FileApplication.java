package com.dau.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FileApplication {

	public static void main(String[] args) {
		// logback-spring.xml에서 사용하기 위해서 PROCESS_NAME에 현재 프로세스 이름을 저장
		System.setProperty("PROCESS_NAME", ProcessHandle.current().info().command().orElse("MyApp"));
		SpringApplication.run(FileApplication.class, args);
	}
}
