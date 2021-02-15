package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
