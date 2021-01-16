package com.game.b1ingservice.service;

import com.game.b1ingservice.postgres.entity.Role;

import java.util.Optional;

public interface RoleService {
    Role getRole(Long id);
}
