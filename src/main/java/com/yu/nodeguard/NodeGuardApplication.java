package com.yu.nodeguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.yu.nodeguard.mapper")
public class NodeGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeGuardApplication.class, args);
    }

}
