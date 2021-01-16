package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Table(name = "role", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_code"})})
@Where(clause = "delete_flag = 0")
public class Role extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_code", columnDefinition = "character varying(50) not null")
    private String roleCode;
    @Column(name = "description", columnDefinition = "character varying(50)")
    private String description;
    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @OneToMany(mappedBy = "role")
    Set<AdminRole> adminRole;

//    @ManyToMany(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "admin_role",
//            joinColumns = {@JoinColumn(name = "role", referencedColumnName = "role_code")},
//            inverseJoinColumns = {@JoinColumn(name = "admins", referencedColumnName = "id")}
//    )
//    private List<AdminUser> adminRole;
}
