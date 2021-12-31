package com.mcdev.javacodingtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "counter")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Counter {

    @Id
    @Column(name = "name", unique = true)
    String name;

    @Column(name = "count")
    int count;
}
