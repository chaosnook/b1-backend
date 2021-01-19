package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bot_server", uniqueConstraints = {@UniqueConstraint(columnNames = {"bot_id"})})
public class Bot_server extends DateAudit<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bot_id", columnDefinition = "character varying(50)")
    private String botId;


    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

}
