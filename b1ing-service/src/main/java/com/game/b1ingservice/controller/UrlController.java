package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.Url.UrlRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.UrlService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping(value = "/url", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createUrl(@RequestBody UrlRequest urlRequest, @AuthenticationPrincipal UserPrincipal principal) {
        urlService.createUrl(urlRequest, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/url")
    public ResponseEntity<?> getUrl(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, urlService.getUrl(principal));

    }

    @PutMapping(value = "/url/{id}")
    public ResponseEntity<?> updateUrl(@PathVariable Long id, @RequestBody UrlRequest req,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        urlService.updateUrl(req, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
