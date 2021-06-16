package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "bank")
@Where(clause = "delete_flag = 0")
public class Bank extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bank_code", columnDefinition = "character varying(50) not null")
    private String bankCode;

    @Column(name = "bank_type", columnDefinition = "character varying(50) not null")
    private String bankType;

    @Column(name = "bank_name", columnDefinition = "character varying(50) not null")
    private String bankName;

    @Column(name = "bank_account_name", columnDefinition = "character varying(60) not null")
    private String bankAccountName;

    @Column(name = "bank_account_no", columnDefinition = "character varying(50) not null")
    private String bankAccountNo;

    @Column(name = "username", columnDefinition = "character varying(50) not null")
    private String username;

    @Column(name = "password", columnDefinition = "character varying(50) not null")
    private String password;

    @Column(name = "bank_order", columnDefinition = "smallint not null")
    private int bankOrder;

    @Column(name = "bank_group", columnDefinition = "smallint not null")
    private int bankGroup;

    @Column(name = "bot_ip", columnDefinition = "character varying(50) not null")
    private String botIp;

    @Column(name = "new_user_flag", columnDefinition = "boolean")
    private boolean newUserFlag;

    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;

    @Column(name = "prefix", columnDefinition = "character varying(50)")
    private String prefix;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @OneToMany(mappedBy = "bank", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Wallet> wallet;


    @OneToMany(mappedBy = "bank", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<DepositHistory> depositHistory;


}
