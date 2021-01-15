package com.game.b1ingservice.postgres.entity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "item", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@Where(clause = "delete_flag = 0")
public class Item implements Serializable {

    @Column(name = "id", columnDefinition = "character varying(50) not null")
    private String id;
    @Column(name = "name", columnDefinition = "character varying(500) not null")
    private String name;
    @Column(name = "quantity", columnDefinition = "character varying(500) not null")
    private String quantity;
    @Column(name = "cost", columnDefinition = "character varying(500) not null")
    private String cost;
    @Column(name = "sale", columnDefinition = "character varying(500) not null")
    private String sale;
}
