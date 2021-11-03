package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "post")
@Entity
@Getter
@Setter
public class Post {
    @Id
    @Column(name = "title", unique = true, nullable = false)
    String title;

    @Column(name = "text")
    String text;

    @Column(name = "categories")
    String categories;

    @Column(name = "author")
    String author;
}
