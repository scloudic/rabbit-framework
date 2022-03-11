package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.example.web.biz.TestBiz;
import com.scloudic.rabbitframework.example.web.rest.Test;
import com.scloudic.rabbitframework.web.AbstractRabbitContextController;
import com.scloudic.rabbitframework.web.Result;
import com.scloudic.rabbitframework.web.annotations.FormValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("test")
public class TestResource extends AbstractRabbitContextController {
    private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
    @Autowired
    private TestBiz testBiz;

    @GetMapping("getData")
    public Result getData() {
        logger.debug("getData");
        return Result.success();
    }

    @GetMapping("getParams")
    @FormValid
    public Result<String> getParams(@NotBlank(message = "{name.null}") @RequestParam("name") String name) {
        String value = testBiz.test(name);
        return Result.success(value);
    }

    @PostMapping("postParams")
    @FormValid
    public Result<Test> postParams(@RequestBody Test test) {
        return Result.success(test);
    }

    @GetMapping("exception")
    public String exception() {
        throw new NullPointerException("异常");
    }
}