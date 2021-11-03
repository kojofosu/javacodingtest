package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "category")
@Entity
@Getter
@Setter
public class Category {
    @Id
    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "posts")
    String posts;
}
