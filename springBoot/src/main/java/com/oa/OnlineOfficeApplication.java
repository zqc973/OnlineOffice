package com.oa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.oa.mapper")
public class OnlineOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineOfficeApplication.class, args);
    }

}
