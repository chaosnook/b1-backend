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
@Table(name = "wallet")
@Where(clause = "delete_flag = 0")
public class Wallet extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "credit", columnDefinition = "numeric(18,2)")
    private BigDecimal credit;

    @Column(name = "point", columnDefinition = "numeric(18,2)")
    private BigDecimal point;

    @Column(name = "turn_over", columnDefinition = "numeric(18,2)")
    private BigDecimal turnOver;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private WebUser user;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "deposit_bank_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "deposit_true_wallet_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TrueWallet trueWallet;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}