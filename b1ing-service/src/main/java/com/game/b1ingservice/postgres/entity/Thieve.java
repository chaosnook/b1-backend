package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "thieve", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@Where(clause = "delete_flag = 0")
public class Thieve extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "character varying(50) not null")
    private String name;
    @Column(name = "bank_name", columnDefinition = "character varying(50) not null")
    private String bankName;
    @Column(name = "bank_account", columnDefinition = "character varying(50) not null")
    private String bankAccount;
    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
