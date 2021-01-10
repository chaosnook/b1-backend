package com.game.b1ingservice.postgres.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {
		"createdDate","updatedDate" }, allowGetters = true)
public class DateAudit<U>{

	@CreatedDate
	@Column(name = "created_date")
	private Instant createdDate;

	@LastModifiedDate
	@Column(name = "updated_date")
	private Instant updatedDate;


	@Column(name = "delete_flag", columnDefinition = "smallint default 0 not null")
	private int deleteFlag = 0;

	@Version
	@Column(name = "version", columnDefinition = "smallint default 1 not null")
	private int version = 1;

}