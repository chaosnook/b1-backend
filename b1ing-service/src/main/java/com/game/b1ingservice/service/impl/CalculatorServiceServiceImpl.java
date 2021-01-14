package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.calculator.CalculatorResponse;
import com.game.b1ingservice.payload.calculator.CalculatorResquest;
import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.entity.CalculatorHistory;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import com.game.b1ingservice.postgres.repository.CalculatorHistoryRepository;
import com.game.b1ingservice.service.ICalculatorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalculatorServiceServiceImpl implements ICalculatorService {

    @Autowired
    private CalculatorHistoryRepository historyRepository;

    @Override
    public ResponseEntity<?> doProcess(CalculatorResquest req) {

        CalculatorResponse resp = new CalculatorResponse();

        double num1 = Double.parseDouble(req.getNumber1());
        double num2 = Double.parseDouble(req.getNumber2());
        String operator = req.getOperator();
        double result = 0L;

        if ("+".equals(req.getOperator())) {
            result = num1 + num2;
        } else if ("-".equals(req.getOperator())) {
            result = num1 - num2;
        } else if ("*".equals(req.getOperator())) {
            result = num1 * num2;
        } else if ("/".equals(req.getOperator())) {
            if(0L == num2) {
                resp.setResult("Cannot divide by zero");
                return ResponseEntity.ok(resp);
            }
            result = num1 / num2;
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        UserAuditEmbeddable audit = new UserAuditEmbeddable();
        audit.setCreatedBy("mix");
        audit.setUpdatedBy("");

        CalculatorHistory history = new CalculatorHistory();
        history.setNumber1(String.valueOf(num1));
        history.setOperator(operator);
        history.setNumber2(String.valueOf(num2));
        history.setResult(String.valueOf(result));
        history.setAudit(audit);

        resp.setResult(String.valueOf(result));

        historyRepository.save(history);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> getHistory(Long id) {

        Optional<CalculatorHistory> opt = historyRepository.findById(id);
        if(opt.isPresent()){
            return ResponseEntity.ok(opt.get());
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_00012);
        }
    }
}
