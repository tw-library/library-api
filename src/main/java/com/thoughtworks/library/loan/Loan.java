package com.thoughtworks.library.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.bookcopy.BookCopy;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "loan")
@Data
public class Loan {

    public Loan() {}

    public Loan(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "bookcopy_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private BookCopy bookCopy;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis());
    }
}