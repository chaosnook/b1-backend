package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.thieve.ThieveRequest;
import com.game.b1ingservice.payload.thieve.ThieveUpdateRequest;
import com.game.b1ingservice.service.ThieveService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.thieve.ThieveValidator;
import com.game.b1ingservice.validator.thieve.ThieveUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/test")
@Slf4j
public class ThieveController {

    @Autowired
    ThieveValidator thieveValidator;
    @Autowired
    ThieveUpdateValidator thieveUpdateValidator;
    @Autowired
    ThieveService thieveService;

    //GetThieveById
    @GetMapping(value = "/getthieve",
        consumes = { MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> thieve(@RequestParam("id") Long id){
        return ResponseEntity.ok(thieveService.getThieve(id));
//        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_01000.msg, thieveService.getThieveList());
    }

    //CreateThieve
    @PostMapping(value = "/addthieve", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addthieve(@RequestBody ThieveRequest thieveRequest){
        thieveValidator.addValidate(thieveRequest);
        thieveService.addThieve(thieveRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_01001.msg);
    }

    //UpdateThieve
    @PutMapping(value = "/updatethieve",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> updateThieve(@RequestBody ThieveUpdateRequest thieveUpdateRequest) {
        thieveUpdateValidator.validate(thieveUpdateRequest);
        thieveService.updateThieve(thieveUpdateRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_01002.msg);
    }

    //DeleteThieve
    @DeleteMapping(value = "/deletethieve",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> deleteThieve(@RequestParam("id") Long id) {
        thieveService.deleteThieve(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_01003.msg);
    }

}
