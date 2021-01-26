package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.webuser.WebUserRequestValidator;
import com.game.b1ingservice.validator.webuser.WebUserUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/test")
public class WebUserController {

    @Autowired
    private WebUserRequestValidator webUserRequestValidator;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebUserUpdateValidator webUserUpdateValidator;

    @PostMapping(value = "/webuser")
    public ResponseEntity<?> createWebUser(@RequestBody WebUserRequest req){
        webUserRequestValidator.validate(req);
        webUserService.createUser(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "/webuser/{id}")
    public ResponseEntity<?> updateWebUser(@PathVariable Long id, @RequestBody WebUserUpdate req){
        webUserUpdateValidator.validate(req);
        webUserService.updateUser(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
