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
@Table(name = "affiliate_history")
@Where(clause = "delete_flag = 0")
public class AffiliateHistory extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point", columnDefinition = "numeric(18,2)")
    private BigDecimal point;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "remark", columnDefinition = "character varying(255)")
    private String remark;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "affiliate_user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebUser userAffiliate;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
