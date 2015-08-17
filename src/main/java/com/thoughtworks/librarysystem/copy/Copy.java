package com.thoughtworks.librarysystem.copy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.loan.Loan;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Table(name="copy")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
public class Copy {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="copy_gen" )
    @SequenceGenerator(name= "copy_gen", sequenceName = "copy_gen")
    private Integer id;

    @Column(name="status")
    private CopyStatus status;

    @Column
    private String donator;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Book book;

    @JoinColumn(name = "library_id", nullable = false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Library library;

    @OneToMany(mappedBy = "copy", cascade = CascadeType.ALL)
    private Set<Loan> loans;

}
