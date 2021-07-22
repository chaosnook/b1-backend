package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.amb.WinLoseReq;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserInfoResponse;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebUserService {

    UserInfoResponse createUser(WebUserRequest req, String prefix);

    void updateUser(Long id, WebUserUpdate req, Long agentId);

    Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable, WebUserSearchRequest request);

    WebUserResetPasswordResponse resetPassword(Long id, UserPrincipal principal);

    boolean verifyTel(String tel, String prefix);

    WebUserHistoryResponse registerHistory(WebUserHistoryRequest webUserHistoryRequest, UserPrincipal principal);

    UserInfoResponse authUser(String username, String password, LoginRequest loginRequest);

    UserProfile getProfile(String username, String prefix);

    GetUserInfoResponse getUserInfo(String username, Long agentId);

    void updateUserWebProfile(String username, String prefix, WebUserProfileUpdate webUserUpdate);

    WebUser getById(Long depositUser);

    List<WinLoseReq> getAllUser(Long agentId);

    void clearCountWithdraw();

//    Page<SearchWebUserDTO> searchWebUser(WebUserSearchRequest request, UserPrincipal principal);


}
