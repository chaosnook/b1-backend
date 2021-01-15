package com.game.b1ingservice.controller;

import com.game.b1ingservice.payload.calculator.CalculatorResponse;
import com.game.b1ingservice.payload.calculator.CalculatorResquest;
import com.game.b1ingservice.service.ICalculatorService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.calculator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test/")
public class CalculatorController {

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private ICalculatorService calculatorService;

    @PostMapping(value = "calculator")
    public ResponseEntity<?> calculator(@RequestBody CalculatorResquest req) {
        requestValidator.validate(req);
        return calculatorService.doProcess(req);
    }

    @GetMapping(value = "getHistory")
    public ResponseEntity<?> getHistory(@RequestParam("id") Long id) {
        return calculatorService.getHistory(id);
    }

    @DeleteMapping(value = "deleteHistory")
    public ResponseEntity<?> deleteHistory(@RequestParam("id") long id) {
        return calculatorService.deleteHistory(id);
    }
}
