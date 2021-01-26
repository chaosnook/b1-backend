package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import org.springframework.stereotype.Service;

@Service
public interface WebUserService {
    void createUser(WebUserRequest req);
    void updateUser(Long id, WebUserUpdate req);
}
