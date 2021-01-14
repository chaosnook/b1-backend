package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.calculator.CalculatorResquest;
import com.game.b1ingservice.postgres.entity.CalculatorHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ICalculatorService {
    ResponseEntity<?> doProcess(CalculatorResquest req);
    ResponseEntity<?> getHistory(Long id);
}
