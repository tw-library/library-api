package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.user.User;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LoanTest {

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

}