package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "items")
@Where(clause = "delete_flag = 0")
public class Item extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "character varying(500) not null")
    private String name;
    @Column(name = "quantity", columnDefinition = "character varying(500) not null")
    private String quantity;
    @Column(name = "cost", columnDefinition = "character varying(500) not null")
    private String cost;
    @Column(name = "sale", columnDefinition = "character varying(500) not null")
    private String sale;
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
