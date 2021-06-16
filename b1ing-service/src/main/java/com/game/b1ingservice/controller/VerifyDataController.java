package com.game.b1ingservice.controller;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/master")
public class VerifyDataController {

    @Autowired
    private WebUserService webUserService;


    @GetMapping(value = "/verifyTel",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> authenticate(@RequestParam("tel") String tel, @RequestParam("prefix") String prefix) {

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, webUserService.verifyTel(tel, prefix));
    }
}
