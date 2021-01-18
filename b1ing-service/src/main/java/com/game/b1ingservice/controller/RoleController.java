package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.repository.AdminUserRepository;
import com.game.b1ingservice.service.IRoleService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.role.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private RoleValidator roleValidator;

    @Autowired
    AdminUserRepository adminUserRepository;

    @PostMapping(value = "/add", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> insertRole(@RequestBody RoleRequest req) {
        roleValidator.validate(req);
        iRoleService.insertRole(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/get")
    public void get() {
        List<AdminUser> x = adminUserRepository.findAll();
        System.out.println(x);
    }
}
