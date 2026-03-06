package com.zheng.acvsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zheng.acvsystem.mapper")   // 扫描mapper接口
public class AcvsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcvsystemApplication.class, args);
    }

}
