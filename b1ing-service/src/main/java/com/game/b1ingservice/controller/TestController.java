package com.game.b1ingservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test/hello")
    @ResponseBody
    public String testSendToSource() {

        return "Hello be1ing";
    }

    @GetMapping("/testtoken")
    @ResponseBody
    public String testtoken() {
        return "Test Token";
    }
}
