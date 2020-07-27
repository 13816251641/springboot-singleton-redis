package com.lujieni.singleton.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.**.dao")
public class SingletonRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingletonRedisApplication.class, args);
    }

}
