package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "loan")
@Data
@EqualsAndHashCode(exclude = {"user" , "email"})
@ToString(exclude = {"user", "email"})
public class Loan {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="loan_gen" )
    @SequenceGenerator(name= "loan_gen", sequenceName = "loan_gen", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="copy_id")
    private Copy copy;

    @Column(name = "start_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Transient
    private String email;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis());
    }

    public String getEmail(){
        return (email == null && this.user != null) ? user.getEmail() : this.email;
    }
}