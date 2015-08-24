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
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="book_gen" )
    @SequenceGenerator(name= "book_gen", sequenceName = "book_gen", allocationSize = 1)
    private Integer id;

    @NotBlank
    @Column
    private String title;

    @NotBlank
    @Column
    private String author;

    @Column
    private String subtitle;

    @Column(columnDefinition="text")
    private String description;

    @Column
    private Long isbn;

    @Column
    private String publisher;

    @Column
    private String publicationDate;

    @Column
    private Integer numberOfPages;

    @Column(columnDefinition="text")
    private String imageUrl;

    public Integer getReference() {
        return id;
    }

}