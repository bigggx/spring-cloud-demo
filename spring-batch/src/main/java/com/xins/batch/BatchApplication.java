package com.xins.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xins.**.mapper")
public class BatchApplication {
    public static void main(String[] args) {
        System.out.println("启动成功");
        SpringApplication.run(BatchApplication.class, args);
    }
}