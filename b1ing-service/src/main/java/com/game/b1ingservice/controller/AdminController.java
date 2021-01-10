package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.auth.SignUpRequest;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.admin.RegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    @Autowired
    RegisterValidator registerValidator;

    @PostMapping(value = "/auth",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Object> authenticate(HttpServletRequest request, @RequestHeader Map<String, String> headers) {


        return ResponseEntity.ok("jwtjwtjwtjwtjwtjwtjwtjwtjwtjwtjwt");
    }

    @PostMapping(path = "/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        registerValidator.validate(signUpRequest);

        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
