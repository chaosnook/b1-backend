package com.game.b1ingservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.game.b1ingservice.payload.thieve.ThieveRequest;

@Service
public interface ThieveService {
    ResponseEntity<?> getThieve(String name);
    void addThieve(ThieveRequest thieveRequest);
}
