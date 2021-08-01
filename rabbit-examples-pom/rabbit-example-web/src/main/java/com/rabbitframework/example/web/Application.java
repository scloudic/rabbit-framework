package com.rabbitframework.example.web;

import com.rabbitframework.web.springboot.RabbitWebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.rabbitframework.example"})
public class Application extends RabbitWebApplication {
    public static void main(String[] args) {
        //new Application().configure(new
          //      SpringApplicationBuilder(Application.class)).run(args);
        SpringApplication.run(Application.class, args);
    }
}