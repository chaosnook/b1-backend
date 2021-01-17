package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.postgres.entity.Role;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IRoleService {
    void insertRole(RoleRequest role);
    ResponseEntity<?> getRole(Long id);
}
