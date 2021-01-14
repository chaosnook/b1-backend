package com.game.b1ingservice.postgres.entity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "admins", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Where(clause = "delete_flag = 0")
public class Item {
}
