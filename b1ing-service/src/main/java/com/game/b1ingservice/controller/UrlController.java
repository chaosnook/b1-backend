package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.Url.UrlRequest;
import com.game.b1ingservice.service.UrlService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class UrlController {
    @Autowired
    UrlService urlService;

    @PostMapping(value = "/url", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> createUrl(@RequestBody UrlRequest urlRequest){
        urlService.createUrl(urlRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
    @GetMapping(value = "/url")
    public  ResponseEntity<?> getUrl(){return urlService.getUrl();}
    @PutMapping(value = "/url/{id}")
    public ResponseEntity<?> updateUrl(@PathVariable Long id,@RequestBody UrlRequest req){
        urlService.updateUrl(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
