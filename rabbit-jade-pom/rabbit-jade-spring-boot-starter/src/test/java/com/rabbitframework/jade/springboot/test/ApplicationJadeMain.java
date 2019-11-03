package com.rabbitframework.jade.springboot.test;

import com.rabbitframework.jade.springboot.configure.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@MapperScan("com.rabbitframework.**.test.mapper")
public class ApplicationJadeMain {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationJadeMain.class, args);
    }
}