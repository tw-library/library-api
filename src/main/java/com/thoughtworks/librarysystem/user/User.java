package com.thoughtworks.librarysystem.user;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="users_gen" )
    @SequenceGenerator(name= "users_gen", sequenceName = "users_gen", allocationSize = 1)
    private Integer id;


    @NotNull
    @NotBlank
    @Email
    @Column(name = "email", unique=true, nullable = false, columnDefinition = "email")
    private String email;

    @NotBlank
    @Column
    private String name;

}