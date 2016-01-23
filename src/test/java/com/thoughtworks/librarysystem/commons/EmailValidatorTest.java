package com.thoughtworks.librarysystem.commons;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EmailValidatorTest {

    @Test
    public void shouldReturnTrueForValidEmail() throws Exception {
        EmailValidator validator = new EmailValidator();
        String validEmail = "valid@email.com";

        assertThat(validator.validate(validEmail), is(true));
    }
}