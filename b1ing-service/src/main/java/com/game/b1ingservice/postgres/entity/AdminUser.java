package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "admins", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Where(clause = "delete_flag = 0")
public class AdminUser extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", columnDefinition = "character varying(50) not null")
    private String username;
    @Column(name = "password", columnDefinition = "character varying(500) not null")
    private String password;
    @Column(name = "tel", columnDefinition = "character varying(50)")
    private String tel;
    @Column(name = "full_name", columnDefinition = "character varying(50) not null")
    private String fullName;
    @Column(name = "limit_flag", columnDefinition = "smallint default 0")
    private int limit = 0;
    @Column(name = "active", columnDefinition = "smallint default 0")
    private int active;
    @Column(name = "prefix", columnDefinition = "character varying(50)")
    private String prefix;
    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne
    private Agent agent;

    @OneToMany(mappedBy = "admin")
    Set<AdminRole> adminRole;

//    @ManyToMany(mappedBy = "adminRole", fetch = FetchType.EAGER)
//    private List<Role> role;

}
