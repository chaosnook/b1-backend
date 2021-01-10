package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    ResponseEntity<?> loginAdmin(String username, String password);
    void registerAdmin(RegisterRequest registerRequest);
}
