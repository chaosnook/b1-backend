package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.payload.role.RoleResponse;
import com.game.b1ingservice.payload.role.RoleUpdateRequest;
import com.game.b1ingservice.postgres.entity.Role;
import com.game.b1ingservice.postgres.repository.RoleRepository;
import com.game.b1ingservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

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
        if(opt.isPresent()){
            Role role = opt.get();
            RoleResponse resp = new RoleResponse();
            resp.setId(role.getId());
            resp.setRoleCode(role.getRoleCode());
            resp.setDescription(role.getDescription());
            resp.setVersion(role.getVersion());
            resp.setCreatedBy(role.getAudit().getCreatedBy());
            resp.setCreatedDate(role.getCreatedDate());
            resp.setUpdatedBy(role.getAudit().getUpdatedBy());
            resp.setUpdatedDate(role.getUpdatedDate());

            return ResponseEntity.ok(resp);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public void updateRole(RoleUpdateRequest req) {
        Optional<Role> opt = roleRepository.findById(req.getId());
        if(opt.isPresent()) {
            Role role = opt.get();
            role.setRoleCode(req.getRoleCode());
            role.setDescription(req.getDescription());
            roleRepository.save(role);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public void deleteRole(Long id) {
        Optional<Role> opt = roleRepository.findByIdAndDeleteFlag(id, 0);
        if(opt.isPresent()) {
            Role role = opt.get();
            role.setDeleteFlag(1);
            roleRepository.save(role);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }
}
