package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.thieve.ThieveRequest;
import com.game.b1ingservice.service.ThieveService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.thieve.ThieveValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/test")
@Slf4j
public class ThieveController {

    @Autowired
    ThieveValidator thieveValidator;
    @Autowired
    ThieveService thieveService;

    @GetMapping(value = "/getthieve",
        consumes = { MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> thieve(@RequestParam("name") String name){
        return ResponseEntity.ok(thieveService.getThieve(name));
//        return ResponseHelper.success(Constants.MESSAGE.MSG_01000.msg);
    }

    @PostMapping(path = "/addthieve", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addthieve(@RequestBody ThieveRequest thieveRequest){
        thieveValidator.addValidate(thieveRequest);
        thieveService.addThieve(thieveRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_01001.msg);
    }
}
