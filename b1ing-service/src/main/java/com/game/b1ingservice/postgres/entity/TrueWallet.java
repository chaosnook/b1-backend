package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "true_wallet")
@Where(clause = "delete_flag = 0")
public class TrueWallet extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", columnDefinition = "character varying(10)")
    private String phoneNumber;

    @Column(name = "name", columnDefinition = "character varying(50) not null")
    private String name;

    @Column(name = "password", columnDefinition = "character varying(50) not null")
    private String password;

    @Column(name = "bank_group", columnDefinition = "smallint not null")
    private int bankGroup;

    @Column(name = "bot_ip", columnDefinition = "character varying(50) not null")
    private String botIp;

    @Column(name = "new_user_flag", columnDefinition = "boolean")
    private boolean newUserFlag;

    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;

    @Column(name = "prefix", columnDefinition = "character varying(50)")
    private String prefix;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

    @OneToMany(mappedBy = "trueWallet", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<Wallet> wallet;

}
