package com.rabbitframework.dbase.springboot.test;

import com.rabbitframework.dbase.springboot.configure.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@MapperScan("com.rabbitframework.**.test.mapper")
public class ApplicationDbaseMain {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationDbaseMain.class, args);
    }
}
