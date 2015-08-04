package com.thoughtworks.librarysystem.book;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Table(name="book")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
public class Book {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="book_gen" )
    @SequenceGenerator(name= "book_gen", sequenceName = "book_gen")
    private Integer id;

    @NotBlank
    @Column(name="title")
    private String title;

    @NotBlank
    @Column(name="author")
    private String author;

}