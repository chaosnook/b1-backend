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
import java.util.Date;

@Data
@Entity
@Table(name = "winlose_history")
@Where(clause = "delete_flag = 0")
public class WinLoseHistory  extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", columnDefinition = "numeric(18,2)")
    private BigDecimal amount;

    @Column(name = "before_amount", columnDefinition = "numeric(18,2)")
    private BigDecimal beforeAmount;

    @Column(name = "after_amount", columnDefinition = "numeric(18,2)")
    private BigDecimal afterAmount;

    @Column(name = "last_date")
    private Date lastDate;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

}
