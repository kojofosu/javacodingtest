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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    String id;

    @Column(name = "title")
    String title;

    @Column(name = "text")
    String text;

    @Column(name = "categories")
    String categories;

    @Column(name = "author")
    String author;
}
