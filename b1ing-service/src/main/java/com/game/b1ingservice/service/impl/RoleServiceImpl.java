package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.postgres.entity.Role;
import com.game.b1ingservice.postgres.repository.RoleRepository;
import com.game.b1ingservice.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void insertRole(RoleRequest req) {
        Role role = new Role();
        role.setRoleCode(req.getRoleCode());
        role.setDescription(req.getDescription());

        roleRepository.save(role);
    }

    @Override
    public ResponseEntity<?> getRole(Long id) {

        Optional<Role> opt = roleRepository.findById(id);
        if(!opt.isPresent()){
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        return ResponseEntity.ok(opt.get());
    }
}
