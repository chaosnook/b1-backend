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
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "username_amb"})})
@Where(clause = "delete_flag = 0")
public class WebUser extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user for web
    @Column(name = "username", columnDefinition = "character varying(50)")
    private String username;

    // user for amb
    @Column(name = "username_amb", columnDefinition = "character varying(50)")
    private String usernameAmb;

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

    @Column(name = "block_bonus", columnDefinition = "boolean default false")
    private Boolean blockBonus;

    @Column(name = "deposit_auto", columnDefinition = "boolean default true")
    private Boolean depositAuto;

    @Column(name = "withdraw_auto", columnDefinition = "boolean default true")
    private Boolean withdrawAuto;

    @Column(name = "deposit_ref")
    private String depositRef;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Wallet wallet;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<AffiliateUser> affiliateUsers;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<DepositHistory> depositHistory;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<PromotionHistory> promotionHistory;

}
