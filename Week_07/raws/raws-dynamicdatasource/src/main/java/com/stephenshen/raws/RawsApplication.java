package com.stephenshen.raws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@MapperScan("com.stephenshen.raws.mapper")//使用MapperScan批量扫描所有的Mapper接口；
public class RawsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RawsApplication.class, args);
    }

}
