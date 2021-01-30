package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.specification.SearchWebUserSpecification;
//import com.game.b1ingservice.utils.PasswordGenerator;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.webuser.WebUserRequestValidator;
import com.game.b1ingservice.validator.webuser.WebUserUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/test")
public class WebUserController {

    @Autowired
    private WebUserRequestValidator webUserRequestValidator;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebUserUpdateValidator webUserUpdateValidator;

    @PostMapping(value = "/webuser")
    public ResponseEntity<?> createWebUser(@RequestBody WebUserRequest req){
        webUserRequestValidator.validate(req);
        return webUserService.createUser(req);
    }

    @PutMapping(value = "/webuser/{id}")
    public ResponseEntity<?> updateWebUser(@PathVariable Long id, @RequestBody WebUserUpdate req){
        webUserUpdateValidator.validate(req);
        webUserService.updateUser(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/webuser/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> search(@RequestBody WebUserSearchRequest request){
        SearchWebUserSpecification specification = new SearchWebUserSpecification(request);
        Page<WebUserResponse> users = webUserService.findByCriteria(specification,specification.getPageable());
        return ResponseHelper.successPage(users, "datas",Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "webuser/reset",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody WebUserUpdate req){
        webUserService.resetPassword(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
