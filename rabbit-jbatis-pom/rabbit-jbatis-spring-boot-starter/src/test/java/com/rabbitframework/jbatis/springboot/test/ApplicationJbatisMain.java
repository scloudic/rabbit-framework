package com.rabbitframework.jbatis.springboot.test;

import com.rabbitframework.jbatis.springboot.configure.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;

import java.io.IOException;

@MapperScan("com.rabbitframework.**.test.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class ApplicationJbatisMain {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationJbatisMain.class, args);
    }
}
