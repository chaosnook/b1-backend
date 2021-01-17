package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="admin_role")
public class AdminRole extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_code", referencedColumnName = "role_code")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private AdminUser admin;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
