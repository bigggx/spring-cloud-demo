package com.xins;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xins.**.mapper")
public class FlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlinkApplication.class, args);
        System.out.println("springflink服务启动成功");
    }
}