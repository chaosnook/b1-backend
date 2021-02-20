package com.game.b1ingservice.payload.amb;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserReq {

    private String memberLoginName;
    private String memberLoginPass;
    private String phoneNo;
    private String contact;
    private String signature;

}
