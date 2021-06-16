package com.game.b1ingservice.postgres.entity;

import com.game.b1ingservice.postgres.entity.audit.DateAudit;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "master_bank")
public class MasterBank extends DateAudit<String> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bank_code", columnDefinition = "character varying(50)")
  private String bankCode;

  @Column(name = "bank_name", columnDefinition = "character varying(50) not null")
   private String bankName;

  @Column(name = "active", columnDefinition = "boolean")
  private boolean active;

  @Column(name = "is_user_bank", columnDefinition = "boolean")
  private boolean isUserBank;

  @Column(name = "is_deposit", columnDefinition = "boolean")
  private boolean isDeposit;

  @Column(name = "is_withdraw", columnDefinition = "boolean")
  private boolean isWithdraw;


    @Embedded
    private UserAuditEmbeddable audit = new UserAuditEmbeddable();
}
