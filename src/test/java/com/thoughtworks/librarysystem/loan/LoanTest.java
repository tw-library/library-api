package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class LoanTest {

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void shouldGetEmailAddressFromUserObjectWhenLoanHasNoEmail() throws Exception {
        String userEmail = "user@email.com";
        User user = new User();
        user.setEmail(userEmail);

        Copy aCopy = new Copy();
        aCopy.setId(1);

        Loan loan = new Loan();
        loan.setCopy(aCopy);
        loan.setUser(user);

        assertThat(loan.getEmail(), is(user.getEmail()));
    }

    @Test
    public void shouldGetEmailAddressFromLoanObjectWhenLoanHasEmail() throws Exception {
        String userEmail = "user@email.com";
        User user = new User();
        user.setEmail(userEmail);

        Copy aCopy = new Copy();
        aCopy.setId(1);

        Loan loan = new Loan();
        loan.setCopy(aCopy);
        loan.setUser(user);
        loan.setEmail("loan@email.com");

        assertThat(loan.getEmail(), is(not(user.getEmail())));
        assertThat(loan.getEmail(), is("loan@email.com"));
    }
}