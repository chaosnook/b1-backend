package com.game.b1ingservice.usercontroller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.point.PointTransRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.service.PointService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class TransactionController {

    @Autowired
    private PointService pointService;

    @PostMapping(value = "/point-transfer")
    public ResponseEntity<?> pointTransfer(@AuthenticationPrincipal UserPrincipal principal, @RequestBody PointTransRequest transRequest) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, pointService.pointTransfer(transRequest, principal.getUsername(), principal.getPrefix()));
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal UserPrincipal principal, @RequestBody WithDrawRequest withDrawRequest) {

        return null;
    }

}
