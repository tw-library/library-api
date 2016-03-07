package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class LoanTest {
    User user;
    Copy aCopy;
    Loan loan;

    private final String USER_EMAIL = "user@email.com";

    @Before
    public void setUp() throws Exception {

        user = new User();
        user.setEmail(USER_EMAIL);

        aCopy = new Copy();
        aCopy.setId(1);

        loan = new Loan();
        loan.setCopy(aCopy);
        loan.setUser(user);

    }

    @Test
    public void shouldGetEmailAddressFromUserObjectWhenLoanHasNoEmail() throws Exception {

        assertThat(loan.getEmail(), is(user.getEmail()));
    }

    @Test
    public void shouldGetEmailAddressFromLoanObjectWhenLoanHasEmail() throws Exception {
        String loanEmail = "loan@email.com";

        loan.setEmail(loanEmail);

        assertThat(loan.getEmail(), is(not(user.getEmail())));
        assertThat(loan.getEmail(), is("loan@email.com"));
    }
}