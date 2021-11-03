package com.mcdev.javacodingtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @JsonIgnoreProperties("posts")
    @JoinTable(name = "category_posts",
            inverseJoinColumns = {@JoinColumn(name = "category_name", referencedColumnName = "name")},
            joinColumns = {@JoinColumn(name = "post_title", referencedColumnName = "title")})
    @ManyToMany
    List<Category> categories;

    @OneToOne
    @JoinColumn(name = "author")
    Author author;
}
