package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.Url.UrlRequest;
import com.game.b1ingservice.payload.Url.UrlResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UrlService {
    void createUrl(UrlRequest urlRequest);
    List<UrlResponse> getUrl(UserPrincipal principal);
    void updateUrl(UrlRequest urlRequest);
}
