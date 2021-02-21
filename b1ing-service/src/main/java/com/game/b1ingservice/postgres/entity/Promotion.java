package com.game.b1ingservice.postgres.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "promotion")
@Where(clause = "delete_flag = 0")
public class Promotion extends DateAudit<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "character varying(50) not null")
    private String name;
    @Column(name = "type", columnDefinition = "character varying(20)")
    private String type;
    @Column(name = "type_bonus", columnDefinition = "character varying(20) not null")
    private String typeBonus;
    @Column(name = "type_promotion", columnDefinition = "character varying(20) not null")
    private String typePromotion;
    @Column(name = "min_topup", columnDefinition = "smallint not null")
    private int minTopup;
    @Column(name = "max_topup", columnDefinition = "smallint not null")
    private int maxTopup;
    @Column(name = "max_bonus", columnDefinition = "smallint not null")
    private int maxBonus;
    @Column(name = "max_withdraw", columnDefinition = "smallint not null")
    private int maxWithdraw;
    @Column(name = "max_receive_bonus", columnDefinition = "smallint not null")
    private int maxReceiveBonus;
    @Column(name = "turn_over", columnDefinition = "smallint not null")
    private int turnOver;
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Temporal(TemporalType.DATE)
    private Date endTime;
    @Column(name = "active", columnDefinition = "boolean")
    private boolean active;
    @Column(name = "url_image", columnDefinition = "character varying(255) not null")
    private String urlImage;

    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();

    @OneToMany(mappedBy = "promotion", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<Condition> condition;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminUser admin;

    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agent agent;

}
