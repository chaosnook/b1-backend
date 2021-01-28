package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebUserService {
    void createUser(WebUserRequest req);
    void updateUser(Long id, WebUserUpdate req);

    List<WebUserResponse> getUserList();
}
