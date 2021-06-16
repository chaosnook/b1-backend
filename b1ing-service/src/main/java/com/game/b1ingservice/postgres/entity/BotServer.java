package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bot_server", uniqueConstraints = {@UniqueConstraint(columnNames = {"bot_ip"})})
@Where(clause = "delete_flag = 0")
public class BotServer extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bot_ip", columnDefinition = "character varying(50)")
    private String botIp;

    @Column(name = "enable", columnDefinition = "boolean default true")
    private Boolean enable;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

}
