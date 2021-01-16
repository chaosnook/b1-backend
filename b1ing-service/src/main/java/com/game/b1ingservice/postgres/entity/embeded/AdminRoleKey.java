package com.game.b1ingservice.postgres.entity.embeded;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AdminRoleKey implements Serializable {

    @Column(name = "admin_id")
    Long adminId;

    @Column(name = "role_id")
    Long roleId;


    // standard constructors, getters, and setters
    // hashcode and equals implementation
}