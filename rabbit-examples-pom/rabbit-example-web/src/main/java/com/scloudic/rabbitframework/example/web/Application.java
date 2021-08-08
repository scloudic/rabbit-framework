package com.scloudic.rabbitframework.example.web;

import com.scloudic.rabbitframework.web.springboot.RabbitWebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.scloudic.rabbitframework.example"})
public class Application extends RabbitWebApplication {
    public static void main(String[] args) {
        //new Application().configure(new
          //      SpringApplicationBuilder(Application.class)).run(args);
        SpringApplication.run(Application.class, args);
    }
}