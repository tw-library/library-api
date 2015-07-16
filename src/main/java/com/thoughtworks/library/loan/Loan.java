package com.thoughtworks.library.loan;

import com.thoughtworks.library.book.Book;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "loan")
@Data
public class Loan {

    public Loan() {}

    public Loan(Book book) {
        this.book = book;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Book book;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis());
    }
}