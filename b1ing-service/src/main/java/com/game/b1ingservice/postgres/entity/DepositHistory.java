package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "deposit_history")
@Where(clause = "delete_flag = 0")
public class DepositHistory extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "bank_code", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "truewallet_code", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TrueWallet trueWallet;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser user;

    @Column(name = "amount", columnDefinition = "numeric(18,2)")
    private BigDecimal amount;

    @Column(name = "before_amount", columnDefinition = "numeric(18,2)")
    private BigDecimal beforeAmount;

    @Column(name = "after_amount", columnDefinition = "numeric(18,2)")
    private BigDecimal afterAmount;

    @Column(name = "type", columnDefinition = "character varying(50)")
    private String type;

    @Column(name = "status", columnDefinition = "character varying(50)")
    private String status;

    @Column(name = "isAuto", columnDefinition = "boolean")
    private Boolean isAuto;

    @Column(name = "reason", columnDefinition = "character varying(100)")
    private String reason;

    @Column(name = "remark", columnDefinition = "character varying(100)")
    private String remark;

    @Column(name = "bonus_amount", columnDefinition = "numeric(18,2)")
    private BigDecimal bonusAmount;

    @Column(name = "transaction_id", columnDefinition = "character varying(100)")
    private String transactionId;

    @Column(name = "first_name", columnDefinition = "character varying(100)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "character varying(100)")
    private String lastName;

    @Column(name = "mobile", columnDefinition = "character varying(50)")
    private String mobile;

    @Column(name = "mistake_type", columnDefinition = "character varying(50)")
    private String mistakeType;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminUser admin;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

}
