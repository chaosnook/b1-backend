package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@DynamicUpdate
@Entity
@Table(name = "promotion_history")
@Where(clause = "delete_flag = 0")
public class PromotionHistory extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Promotion promotion;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser user;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @Column(name = "topUp", columnDefinition = "numeric(18,2)")
    private BigDecimal topup;

    @Column(name = "bonus", columnDefinition = "numeric(18,2)")
    private BigDecimal bonus;

    @Column(name = "turn_over", columnDefinition = "numeric(18,2)")
    private BigDecimal turnOver;

    @Column(name = "transaction_id", columnDefinition = "character varying(50) not null")
    private String transactionId;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

}
