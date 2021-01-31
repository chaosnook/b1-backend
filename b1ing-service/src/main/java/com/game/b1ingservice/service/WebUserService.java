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

@Service
public interface WebUserService {
    WebUserResponse createUser(WebUserRequest req);
    void updateUser(Long id, WebUserUpdate req);

    Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable);

    ResponseEntity<?> resetPassword(Long id);

}
