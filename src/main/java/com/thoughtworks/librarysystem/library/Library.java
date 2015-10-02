package com.thoughtworks.librarysystem.library;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Table(name="library")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
@EqualsAndHashCode(exclude={"copies"})
public class Library {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="library_gen" )
    @SequenceGenerator(name= "library_gen", sequenceName = "library_gen", allocationSize = 1)
    private Integer id;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String slug;

}