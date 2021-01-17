package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.service.IRoleService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.role.InsertValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test/")
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private InsertValidator insertValidator;

    @PostMapping(value = "addRole", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> insertRole(@RequestBody RoleRequest req) {
        insertValidator.validate(req);
        iRoleService.insertRole(req);

        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
