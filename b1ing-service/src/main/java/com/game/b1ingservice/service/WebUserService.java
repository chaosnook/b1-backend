package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebUserService {
    ResponseEntity<?> createUser(WebUserRequest req);
    void updateUser(Long id, WebUserUpdate req);

    List<WebUserResponse> getUserList();
    Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable);
}
