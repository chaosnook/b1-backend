package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.condition.ConditionRequest;
import com.game.b1ingservice.service.ConditionService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class ConditionController {

    @Autowired
    private ConditionService conditionService;

    //insert condition
    @PostMapping(value = "condition",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> insertCondition(@RequestBody ConditionRequest conditionRequest,
                                             @AuthenticationPrincipal UserPrincipal principal) {
        conditionService.insertCondition(conditionRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    //get condition
    @GetMapping(value = "/condition",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getCondition(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, conditionService.getCondition(principal));

    }

    //update condition
    @PutMapping(value = "condition/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateCondition(@PathVariable Long id, @RequestBody ConditionRequest conditionRequest,
                                             @AuthenticationPrincipal UserPrincipal principal) {
        conditionService.updateCondition(id, conditionRequest, principal);
        return  ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    //delete condition
    @DeleteMapping(value = "condition/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteCondition(@PathVariable Long id) {
        conditionService.deleteCondition(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
