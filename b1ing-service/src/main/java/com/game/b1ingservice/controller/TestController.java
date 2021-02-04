package com.game.b1ingservice.controller;

import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {
    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test/hello")
    @ResponseBody
    public Object testSendToSource() {
        return webUserJdbcRepository.summaryRegisterUsersByDay("2021-02-01");
    }

    @GetMapping("/testtoken")
    @ResponseBody
    public String testtoken() {
        return "Test Token";
    }

}
