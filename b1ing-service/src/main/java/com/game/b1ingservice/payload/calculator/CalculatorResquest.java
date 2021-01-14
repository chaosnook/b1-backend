package com.game.b1ingservice.payload.calculator;

import lombok.Data;

@Data
public class CalculatorResquest {
    private String number1;
    private String number2;
    private String operator;
}
