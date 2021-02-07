package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WebUserService {
    WebUserResponse createUser(WebUserRequest req, UserPrincipal principal);
    void updateUser(Long id, WebUserUpdate req);

    Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable);

    ResponseEntity<?> resetPassword(Long id, WebUserUpdate webUserUpdate);

//    WebUserHistoryResponse registerHistory(WebUserHistoryRequest webUserHistoryRequest);

}
