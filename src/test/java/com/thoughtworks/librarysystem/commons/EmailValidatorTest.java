package com.thoughtworks.librarysystem.commons;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EmailValidatorTest {

    private EmailValidator validator;

    @Before
    public void setUp() {
        validator = new EmailValidator();

    }

    @Test
    public void shouldReturnTrueForValidEmail() {
        String validEmail = "valid@email.com";

        assertThat(validator.validate(validEmail), is(true));
    }

    @Test
    public void shouldReturnFalseForInvalidEmail() {
        String invalidEmail = "invalid@email";

        assertThat(validator.validate(invalidEmail), is(false));

        String anotherInvalidEmail = "invalid.com";
        
        assertThat(validator.validate(anotherInvalidEmail), is(false));
    }
}