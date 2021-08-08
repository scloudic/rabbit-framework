package com.scloudic.rabbitframework.security.springboot.configure.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

@SpringBootApplication
@WebAppConfiguration
public class ApplicationSecurityMain {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationSecurityMain.class, args);
    }
}
