package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.payload.role.RoleUpdateRequest;
import com.game.b1ingservice.service.RoleService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.role.RequestValidator;
import com.game.b1ingservice.validator.role.UpdateRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private UpdateRequestValidator updateRequestValidator;

    @PostMapping(value = "/insert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> insertRole(@RequestBody RoleRequest req) {
        requestValidator.validate(req);
        roleService.insertRole(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getRole(@RequestParam("id") Long id) {
        return roleService.getRole(id);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateRole(@RequestBody RoleUpdateRequest req) {
        updateRequestValidator.validate(req);
        roleService.updateRole(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteRole(@RequestParam("id") Long id) {
        roleService.deleteRole(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
