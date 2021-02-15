package com.game.b1ingservice.usercontroller;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class PromotionUserController {



    @GetMapping(value = "/promotion")
    public ResponseEntity<?> promotion(@AuthenticationPrincipal UserPrincipal principal) {


        return null;
    }
}
