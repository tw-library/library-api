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

    @Test
    public void shouldReturnFalseForInvalidEmail() throws Exception {
        EmailValidator validator = new EmailValidator();
        String invalidEmail = "invalid@email";

        assertThat(validator.validate(invalidEmail), is(false));

        String anotherInvalidEmail = "invalid.com";
        assertThat(validator.validate(anotherInvalidEmail), is(false));
    }
}