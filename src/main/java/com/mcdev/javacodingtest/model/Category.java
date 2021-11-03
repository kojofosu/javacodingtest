package com.mcdev.javacodingtest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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


    @ManyToMany(mappedBy = "categories")
    List<Post> posts;
}
