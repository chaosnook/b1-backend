package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.Url.UrlRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    void createUrl(UrlRequest urlRequest);
    ResponseEntity<?> getUrl();
    void updateUrl(UrlRequest urlRequest);
}
