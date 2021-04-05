package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositList;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserInfoResponse;
import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import com.game.b1ingservice.postgres.jdbc.dto.SearchWebUserDTO;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.specification.SearchWebUserSpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.webuser.GetWebUserInfoRequestValidator;
import com.game.b1ingservice.validator.webuser.WebUserRequestValidator;
import com.game.b1ingservice.validator.webuser.WebUserUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "api/admin")
public class WebUserController {

    @Autowired
    private WebUserRequestValidator webUserRequestValidator;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebUserUpdateValidator webUserUpdateValidator;

    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;

    @Autowired
    private GetWebUserInfoRequestValidator getWebUserInfoRequestValidator;

    @PostMapping(value = "/webuser")
    public ResponseEntity<?> createWebUser(@RequestBody WebUserRequest req, @AuthenticationPrincipal UserPrincipal principal) {
        webUserRequestValidator.validate(req);
        UserInfoResponse resp = webUserService.createUser(req, principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resp);
    }

    @PutMapping(value = "/webuser/{id}")
    public ResponseEntity<?> updateWebUser(@PathVariable Long id, @RequestBody WebUserUpdate req) {
        webUserUpdateValidator.validate(req);
        webUserService.updateUser(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

//    @PostMapping(value = "/webuser/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> search(@RequestBody WebUserSearchRequest request){
//        SearchWebUserSpecification specification = new SearchWebUserSpecification(request);
//        Page<WebUserResponse> users = webUserService.findByCriteria(specification,specification.getPageable());
//        return ResponseHelper.successPage(users, "datas",Constants.MESSAGE.MSG_00000.msg);
//    }

    @PostMapping(value = "/webuser/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> search(@RequestBody WebUserSearchRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        List<SearchWebUserDTO> obj = webUserService.searchWebUser(request, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }


//    @GetMapping(value = "/list-user-deposit")
//    public ResponseEntity<?> listDepositUser(@AuthenticationPrincipal UserPrincipal principal){
//        List<WebUserResponse> res = webUserService.listDepositUser();
//        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg,res);
//    }

    @PutMapping(value = "webuser/reset/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        WebUserResetPasswordResponse resp = webUserService.resetPassword(id, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resp);
    }

    @PostMapping("/webuser/reghistory")
    @ResponseBody
    public ResponseEntity<?> registerHistoryReport(@RequestBody WebUserHistoryRequest webUserHistoryRequest, @AuthenticationPrincipal UserPrincipal principal) {
        WebUserHistoryResponse obj = webUserService.registerHistory(webUserHistoryRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }

    @PostMapping("/webuser/getInfo")
    public ResponseEntity<?> getUserInfo(@RequestBody GetUserInfoRequest req, @AuthenticationPrincipal UserPrincipal principal) {
        getWebUserInfoRequestValidator.validate(req);
        GetUserInfoResponse response = webUserService.getUserInfo(req.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }
}
