package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.postgres.entity.Role;
import com.game.b1ingservice.postgres.repository.RoleRepository;
import com.game.b1ingservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRole(Long id) {
        Optional<Role> opt = roleRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        return null;
    }
}
