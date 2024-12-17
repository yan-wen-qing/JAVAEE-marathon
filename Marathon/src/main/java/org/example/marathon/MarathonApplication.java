package org.example.marathon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MarathonApplication {
    public static void main(String[] args) {
        log.info("Spring Boot 正在启动...");
        SpringApplication.run(MarathonApplication.class, args);
        log.info("Spring Boot 启动完成");
    }
}
