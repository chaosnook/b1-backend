package com.game.b1ingservice.payload.thieve;

import lombok.Data;

@Data
public class ThieveRequest {
    private Long id;
    private String name;
    private String bankName;
    private String bankAccount;
}
