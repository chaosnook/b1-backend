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
@Table(name = "withdraw_history")
@Where(clause = "delete_flag = 0")
public class WithdrawHistory  extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "qr_code", columnDefinition = "character varying(100)")
    private String qrCode;

    @Column(name = "before_transfer", columnDefinition = "character varying(225)")
    private String beforeTransfer;

    @Column(name = "after_transfer", columnDefinition = "character varying(225)")
    private String afterTransfer;

    @Column(name = "remain_balance", columnDefinition = "numeric(18,2)")
    private BigDecimal remainBalance;

    @Column(name = "mistake_type", columnDefinition = "character varying(50)")
    private String mistakeType;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminUser admin;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "bank_code", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser user;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
