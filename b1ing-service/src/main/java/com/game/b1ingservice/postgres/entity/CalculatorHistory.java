package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CALCULATOR_HISTORY")
public class CalculatorHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMBER_1", columnDefinition = "character varying(10) not null")
    private String number1;
    @Column(name = "OPERATOR", columnDefinition = "character varying(1) not null")
    private String operator;
    @Column(name = "NUMBER_2", columnDefinition = "character varying(10) not null")
    private String number2;
    @Column(name = "RESULT", columnDefinition = "character varying(10) not null")
    private String result;
    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
