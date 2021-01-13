package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.LoginResponse;
import com.game.b1ingservice.payload.admin.RegisterRequest;
import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.repository.AdminUserRepository;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.utils.JwtTokenUtil;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminUserRepository adminUserRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Override
    public ResponseEntity<?> loginAdmin(String username, String password) {
        Optional<AdminUser> opt = adminUserRepository.findByUsername(username);
        if (opt.isPresent()){
            AdminUser admin = opt.get();
            if (bCryptPasswordEncoder.matches(password,admin.getPassword())){
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", admin.getId());
                claims.put("username", admin.getUsername());
                if (ObjectUtils.isNotEmpty(admin.getAgent()))
                    claims.put("agentId", admin.getAgent().getId());
                claims.put("type", "ADMIN");

                return ResponseEntity.ok(new LoginResponse(jwtTokenUtil.generateToken(claims, "user")));
            }
        }
        return ResponseHelper.bad(Constants.ERROR.ERR_00007.msg);

    }

    @Override
    public void registerAdmin(RegisterRequest registerRequest) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(registerRequest.getUsername());
        adminUser.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        adminUser.setTel(registerRequest.getTel());
        adminUser.setFullName(registerRequest.getFullName());
        adminUser.setLimit(registerRequest.getLimit());
        adminUser.setActive(registerRequest.getActive());
        adminUser.setPrefix(registerRequest.getPrefix());
        adminUserRepository.save(adminUser);
    }


}
