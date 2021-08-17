package com.scloudic.rabbitframework.jbatis.springboot.test;

import com.scloudic.rabbitframework.jbatis.springboot.configure.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@MapperScan("com.scloudic.rabbitframework.**.test.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApplicationJbatisMain {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationJbatisMain.class, args);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager(@Qualifier("datasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
