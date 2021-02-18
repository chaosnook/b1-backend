package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.admin.ProfitReportResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.payload.webuser.WebUserHistoryResponse;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.service.TopUpService;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.webuser.WebUserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class TopUpController {

    @Autowired
    TopUpService topUpService;

    @PostMapping(value = "/topup-report")
    @ResponseBody
    public ResponseEntity<?> topupReport(@RequestBody ProfitReportRequest profitReportRequest, @AuthenticationPrincipal UserPrincipal principal) {
        ProfitReportResponse obj = topUpService.topupReport(profitReportRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }
}
