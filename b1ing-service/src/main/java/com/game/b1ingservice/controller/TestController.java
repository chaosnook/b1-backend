package com.game.b1ingservice.controller;

import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {
    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;

    @Autowired
    private AMBService ambService;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

//    @GetMapping("/test/hello")
//    @ResponseBody
//    public Object testSendToSource() {
//        return webUserJdbcRepository.summaryRegisterUsersByDay("2021-02-01");
//    }

    @GetMapping("/testtoken")
    @ResponseBody
    public String testtoken() {
        return "Test Token";
    }


    @GetMapping("/test/amb")
    public ResponseEntity<?> testSendToSource() {
        ambService.getCredit("VBKK0000000");
        ambService.deposit(DepositReq.builder().amount("10.00").build(), "VBKK0000000");
        return ResponseHelper.success("ok");
    }

}
