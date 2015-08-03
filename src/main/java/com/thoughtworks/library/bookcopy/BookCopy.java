package com.thoughtworks.library.bookcopy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.loan.Loan;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Set;

@Table(name="bookcopy")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="status")
    protected BookCopyStatus status;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Book book;

    @OneToMany(mappedBy = "bookCopy", cascade = CascadeType.ALL)
    private Set<Loan> loans;

}
