package com.dau.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FileApplication {

	public static void main(String[] args) {
		System.setProperty("PROCESS_NAME", ProcessHandle.current().info().command().orElse("MyApp"));
		SpringApplication.run(FileApplication.class, args);
	}
}
