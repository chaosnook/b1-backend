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
import java.util.List;

@Data
@Entity
@Table(name = "affiliate")
@Where(clause = "delete_flag = 0")
public class Affiliate extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_bonus", columnDefinition = "character varying(255)")
    private String typeBonus;

    @Column(name = "max_bonus", columnDefinition = "numeric(18,2)")
    private BigDecimal maxBonus;

    @Column(name = "max_wallet", columnDefinition = "numeric(18,2)")
    private BigDecimal maxWallet;

    @Column(name = "max_withdraw", columnDefinition = "numeric(18,2)")
    private BigDecimal maxWithdraw;

    @Column(name = "img", columnDefinition = "character varying(255)")
    private String img;

    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;

    @Column(name = "is_forever", columnDefinition = "boolean")
    private boolean isForever;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @OneToMany(mappedBy = "affiliate", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<AffiliateCondition> condition;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
