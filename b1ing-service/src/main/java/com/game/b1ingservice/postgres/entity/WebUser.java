package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Where(clause = "delete_flag = 0")
public class WebUser extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", columnDefinition = "character varying(50)")
    private String username;
    @Column(name = "password", columnDefinition = "character varying(500)")
    private String password;
    @Column(name = "tel", columnDefinition = "character varying(50)")
    private String tel;
    @Column(name = "bank_name", columnDefinition = "character varying(50)")
    private String bankName;
    @Column(name = "account_number", columnDefinition = "character varying(50)")
    private String accountNumber;
    @Column(name = "first_name", columnDefinition = "character varying(50)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "character varying(50)")
    private String lastName;
    @Column(name = "line", columnDefinition = "character varying(50)")
    private String line;
    @Column(name = "is_bonus", columnDefinition = "character varying(50)")
    private String isBonus;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne
    private Agent agent;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Wallet wallet;

}
