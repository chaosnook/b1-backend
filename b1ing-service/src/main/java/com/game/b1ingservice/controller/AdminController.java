package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.CountRefillDTO;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.admin.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    @Autowired
    private RegisterValidator registerValidator;
    @Autowired
    private UpdateValidator updateValidator;
    @Autowired
    private PrefixValidator prefixValidator;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AddCreditValidator addCreditValidator;
    @Autowired
    private WithdrawValidator withdrawValidator;

    @PostMapping(value = "/auth",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> authenticate(@RequestHeader Map<String, String> headers,
                                          @RequestBody LoginRequest loginRequest) {

        String basic = headers.get("authorization");
        String base64 = basic.substring(6);
        String decode = new String(Base64.decodeBase64(base64.getBytes(Charset.forName("US-ASCII"))));
        String[] arr = decode.split(":");
        if (arr.length == 2)
            return adminService.loginAdmin(arr[0], arr[1], loginRequest);
        else
            return ResponseHelper.bad(Constants.ERROR.ERR_00007.msg);
    }

    @PostMapping(path = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, @AuthenticationPrincipal UserPrincipal principal) {
        registerValidator.validate(registerRequest);
        adminService.registerAdmin(registerRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(path = "/update-admin", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> update(@RequestBody AdminUpdateRequest adminUpdateRequest, @AuthenticationPrincipal UserPrincipal principal) {
        updateValidator.validate(adminUpdateRequest);
        adminService.updateAdmin(adminUpdateRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/user-list/{prefix}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> authenticate(@RequestHeader Map<String, String> headers,
                                          @PathVariable("prefix") String prefix,
                                          @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, adminService.listByPrefix(prefix));
    }

    @GetMapping(value = "/get/{username}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getUser(@PathVariable("username") String username,
                                     @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, adminService.findAdminByUsernamePrefix(username, principal.getPrefix()));
    }

    @PostMapping(value = "/deposit", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deposit(@RequestBody AddCreditRequest req, @AuthenticationPrincipal UserPrincipal principal) {
        addCreditValidator.validate(req);
        adminService.addCredit(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/withdraw", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest req, @AuthenticationPrincipal UserPrincipal principal) {
        withdrawValidator.validate(req);
        adminService.withdrawCredit(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/profit-report")
    @ResponseBody
    public ResponseEntity<?> profitReport(@RequestBody ProfitReportRequest profitReportRequest, @AuthenticationPrincipal UserPrincipal principal) {
        ProfitReportResponse obj = adminService.profitReport(profitReportRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }

    @PostMapping(value = "/profit-loss", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> profitLoss(@RequestBody ProfitLossRequest profitLossRequest, @AuthenticationPrincipal UserPrincipal principal) {
        ProfitLossResponse obj = adminService.profitLoss(profitLossRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }

    @PostMapping(value = "/count-refill", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> countRefill(@RequestBody CountRefillRequest countRefillRequest, @AuthenticationPrincipal UserPrincipal principal) {
        List<CountRefillDTO> obj = adminService.countRefill(countRefillRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }

    @PostMapping(path = "/withdraw/manual", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> update(@RequestBody WithdrawManualReq req, @AuthenticationPrincipal UserPrincipal principal) {
        adminService.withdrawManual(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(path = "/approve", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> update(@RequestBody ApproveReq req, @AuthenticationPrincipal UserPrincipal principal) {
        adminService.approve(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }


}
