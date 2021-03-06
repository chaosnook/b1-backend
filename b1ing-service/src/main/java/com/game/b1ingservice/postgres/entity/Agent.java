package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "agent", uniqueConstraints = {@UniqueConstraint(columnNames = {"prefix"})})
@Where(clause = "delete_flag = 0")
public class Agent extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // for AMB prefix
    @NotNull
    @Column(name = "company_name", columnDefinition = "character varying(50)")
    private String companyName;

    // for web prefix
    @NotNull
    @Column(name = "prefix", columnDefinition = "character varying(50)")
    private String prefix;

    @Column(name = "website", columnDefinition = "character varying(50)")
    private String website;
    @Column(name = "line_id", columnDefinition = "character varying(50)")
    private String lineId;
    @Column(name = "logo", columnDefinition = "character varying(500)")
    private String logo;
    @Column(name = "background", columnDefinition = "character varying(500)")
    private String background;
    @Column(name = "line_token", columnDefinition = "character varying(500)")
    private String lineToken;

    @Column(name = "line_token_withdraw", columnDefinition = "character varying(500)")
    private String lineTokenWithdraw;

    @Column(name = "key", columnDefinition = "character varying(50)")
    private String key;
    @Column(name = "clientname", columnDefinition = "character varying(50)")
    private String clientName;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @ToString.Exclude
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.MERGE,CascadeType.PERSIST} , fetch = FetchType.LAZY)
    private List<WebUser> webUsers;

    @ToString.Exclude
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<AdminUser> adminUsers;

    @Where(clause = "type = 'AGENT-CONFIG'")
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Config> configs;

    @ToString.Exclude
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.MERGE,CascadeType.PERSIST} , fetch = FetchType.LAZY)
    private List<Promotion> promotions;

    @ToString.Exclude
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.MERGE,CascadeType.PERSIST} , fetch = FetchType.LAZY)
    private List<PromotionHistory> promotionHistory ;

}
