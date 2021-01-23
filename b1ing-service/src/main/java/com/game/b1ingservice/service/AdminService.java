package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.AdminUpdateRequest;
import com.game.b1ingservice.payload.admin.AdminUserResponse;
import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.admin.RegisterRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    ResponseEntity<?> loginAdmin(String username, String password, LoginRequest loginRequest);
    void registerAdmin(RegisterRequest registerRequest, UserPrincipal principal);
    void updateAdmin(AdminUpdateRequest adminUpdateRequest, UserPrincipal principal);
    List<AdminUserResponse> listByPrefix(String prefix);

    AdminUserResponse findAdminByUsernamePrefix(String username, String prefix);
}
