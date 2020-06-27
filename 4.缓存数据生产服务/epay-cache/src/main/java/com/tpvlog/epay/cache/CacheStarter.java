package com.tpvlog.epay.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = "com.tpvlog.epay.cache")
@MapperScan("com.tpvlog.epay.cache.mapper")
@ServletComponentScan
public class CacheStarter {
    public static void main(String[] args) {
        SpringApplication.run(CacheStarter.class, args);
    }
}