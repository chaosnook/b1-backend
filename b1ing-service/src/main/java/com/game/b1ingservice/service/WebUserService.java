package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.userinfo.UserInfoResponse;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WebUserService {

    UserInfoResponse createUser(WebUserRequest req, String prefix);

    void updateUser(Long id, WebUserUpdate req);

    Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable);

    ResponseEntity<?> resetPassword(Long id, WebUserUpdate webUserUpdate);

    UserInfoResponse authUser(String username, String password, LoginRequest loginRequest);

    UserProfile getProfile(String username, String prefix);
//    WebUserHistoryResponse registerHistory(WebUserHistoryRequest webUserHistoryRequest);

}
