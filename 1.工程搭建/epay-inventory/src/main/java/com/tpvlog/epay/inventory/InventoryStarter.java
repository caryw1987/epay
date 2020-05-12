package com.tpvlog.epay.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = "com.tpvlog.epay.inventory")
@MapperScan("com.tpvlog.epay.inventory.mapper")
@ServletComponentScan
public class InventoryStarter {

    public static void main(String[] args) {
        SpringApplication.run(InventoryStarter.class, args);
    }
 
}