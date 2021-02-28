package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.AddCreditRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.misktake.*;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.service.MistakeService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/admin")
public class MistakeController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MistakeService mistakeService;

    @PostMapping(value = "/mistake", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> mistake(@RequestBody MistakeReq req, @AuthenticationPrincipal UserPrincipal principal) {
        mistakeService.createMistake(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/mistake/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> mistake(@RequestBody MistakeSearchReq req, @AuthenticationPrincipal UserPrincipal principal) {
        List<MistakeSearchListRes> resList = mistakeService.findByCriteria(req, principal);
        MistakeSearchSummaryRes summaryRes = mistakeService.summaryData(resList);
        MistakeSearchRes mistakeSearchRes = new MistakeSearchRes();
        mistakeSearchRes.setList(resList);
        mistakeSearchRes.setSummary(summaryRes);

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, mistakeSearchRes);
    }
}
