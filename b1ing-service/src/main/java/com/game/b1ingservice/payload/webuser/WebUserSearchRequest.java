package com.game.b1ingservice.payload.webuser;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WebUserSearchRequest extends SearchPageable {

    private String bankName;
    private String username;
    private String accountNumber;
    private String tel;
    private String firstName;
    private String lastName;
    private String fullName;
    private String type;
    private int typeUser;
    private String searchValue;
    
    private String createdDateFrom;
    private String createdDateTo;
}
