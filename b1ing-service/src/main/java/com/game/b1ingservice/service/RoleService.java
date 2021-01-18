package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.payload.role.RoleUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService {
    void insertRole(RoleRequest req);
    ResponseEntity<?> getRole(Long id);
    void updateRole(RoleUpdateRequest req);
    void deleteRole(Long id);
}
