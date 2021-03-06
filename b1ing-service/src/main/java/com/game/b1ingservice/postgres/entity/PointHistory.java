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
@Table(name = "point_history")
@Where(clause = "delete_flag = 0")
public class PointHistory extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser user;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "deposit_user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser userDep;

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

    @Column(name = "reason", columnDefinition = "character varying(100)")
    private String reason;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminUser admin;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
