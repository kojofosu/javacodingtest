package com.mcdev.javacodingtest.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "author")
@Entity
@Getter
@Setter
public class Author {
    @Column(name = "firstname")
    String firstname;

    @Column(name = "lastname")
    String lastname;

    @Id
    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "phone", length = 10)
    String phone;
}
