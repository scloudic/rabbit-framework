package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.web.AbstractRabbitController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ErrorResource extends AbstractRabbitController {
    @GetMapping("404")
    public Object to404() {
        return null;
    }
}
