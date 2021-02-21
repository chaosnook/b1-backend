package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.ProfitReportResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.topup.TopUpResponse;
import com.game.b1ingservice.payload.topup.TopupRequest;
import com.game.b1ingservice.service.TopUpService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> topUpReport(@RequestBody TopupRequest topupRequest, @AuthenticationPrincipal UserPrincipal principal) {
        TopUpResponse obj = topUpService.topUpReport(topupRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }
}
