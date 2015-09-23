package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.user.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class LoanBuilder {

    private Integer id;
    private Copy copy;
    private Date startDate;
    private Date endDate;
    private User user;

    public Loan build(){

        Loan loan = new Loan();

        loan.setCopy(copy);
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setUser(user);
        loan.setId(id);
        loan.setUser(user);

        return loan;
    }

}
