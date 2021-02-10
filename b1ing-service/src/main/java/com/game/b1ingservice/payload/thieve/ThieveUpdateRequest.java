package com.game.b1ingservice.payload.thieve;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThieveUpdateRequest implements Serializable {
    private Long id;
    private String name;
    private String bankName;
    private String bankAccount;
}
