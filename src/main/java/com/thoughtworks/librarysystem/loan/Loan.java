package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "loan")
@Data
public class Loan {

    public Loan() {}

    public Loan(Copy copy) {
        this.copy = copy;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="loan_gen" )
    @SequenceGenerator(name= "loan_gen", sequenceName = "loan_gen")
    private Integer id;

    @JoinColumn(name = "copy_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Copy copy;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis());
    }
}