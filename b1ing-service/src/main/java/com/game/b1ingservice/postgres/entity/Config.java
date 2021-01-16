package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "config", uniqueConstraints = {@UniqueConstraint(columnNames = {"parameter"})})
@Where(clause = "delete_flag = 0")
public class Config extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parameter", columnDefinition = "character varying(50)")
    private String parameter;
    @Column(name = "value", columnDefinition = "character varying(50)")
    private String value;
    @Column(name = "type", columnDefinition = "character varying(50)")
    private String type;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne
    private Agent agent;

}
