package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.thieve.ThieveUpdateRequest;
import com.game.b1ingservice.postgres.entity.Thieve;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.game.b1ingservice.payload.thieve.ThieveRequest;

import java.util.List;

@Service
public interface ThieveService {
    ResponseEntity<?> getThieve(Long id);
//    List<Thieve> getThieveList();
    void addThieve(ThieveRequest thieveRequest);
    void updateThieve(ThieveUpdateRequest thieveUpdateRequest);
    void deleteThieve(Long id);
}
