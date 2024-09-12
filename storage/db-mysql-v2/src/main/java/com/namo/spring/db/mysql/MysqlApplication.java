package com.namo.spring.db.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MySQL 동작을 테스트 하기 위한 메인 클래스
 */
@SpringBootApplication
public class MysqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlApplication.class, args);
    }
}
