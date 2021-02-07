package com.game.b1ingservice.usercontroller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.agent.AgentInfoRequest;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.userinfo.UserWalletResponse;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.service.AgentService;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.specification.SearchWebUserSpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.webuser.WebUserRequestValidator;
import com.game.b1ingservice.validator.webuser.WebUserUpdateValidator;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private WebUserRequestValidator webUserRequestValidator;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WebUserUpdateValidator webUserUpdateValidator;

    @Autowired
    private AgentService agentService;

    @PostMapping(value = "/prefix")
    public ResponseEntity<?> prefixInfo(@RequestBody AgentInfoRequest req) {
        AgentResponse agentResponse = agentService.getAgentByPrefix(req.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, agentResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerWebUser(@RequestBody WebUserRequest req) {
        webUserRequestValidator.validate(req);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, webUserService.createUser(req, req.getPrefix()));
    }

    @PostMapping(value = "/auth",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> authenticate(@RequestHeader(name = "authorization") String authorization,
                                          @RequestBody LoginRequest loginRequest) {

        String base64 = authorization.substring(6);
        String decode = new String(Base64.decodeBase64(base64.getBytes(StandardCharsets.US_ASCII)));
        String[] arr = decode.split(":");
        if (arr.length == 2) {
            return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, webUserService.authUser(arr[0], arr[1], loginRequest));
        } else {
            return ResponseHelper.bad(Constants.ERROR.ERR_00007.msg);
        }
    }

    @PostMapping(value = "/wallet")
    public ResponseEntity<?> walletInfo(@AuthenticationPrincipal UserPrincipal principal) {
        UserWalletResponse response = walletService.getUserWallet(principal.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal UserPrincipal principal) {
        UserProfile response = webUserService.getProfile(principal.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }

}
