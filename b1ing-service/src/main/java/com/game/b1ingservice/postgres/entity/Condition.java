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
@Table(name = "condition")
@Where(clause = "delete_flag = 0")
public class Condition extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_topup", columnDefinition = "numeric(18,2)")
    private BigDecimal minTopup;

    @Column(name = "max_topup", columnDefinition = "numeric(18,2)")
    private BigDecimal maxTopup;

    @Column(name = "bonus", columnDefinition = "numeric(18,2)")
    private BigDecimal bonus;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    @ManyToOne
    private Promotion promotion;

}
