package com.thoughtworks.librarysystem.loan;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.librarysystem.copy.Copy;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Entity
@Table(name = "loan")
@Data
public class Loan {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="loan_gen" )
    @SequenceGenerator(name= "loan_gen", sequenceName = "loan_gen")
    private Integer id;

    @JoinColumn(name = "copy_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Copy copy;

    @Column(name = "start_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @NotBlank
    @Column(name = "email")
    @Email
    private String email;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis());
    }
}