package com.stephenshen.raws;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stephenshen.raws.mapper")//使用MapperScan批量扫描所有的Mapper接口；
public class RawsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RawsApplication.class, args);
    }

}
