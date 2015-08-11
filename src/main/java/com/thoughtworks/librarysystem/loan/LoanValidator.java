package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.commons.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
public class LoanValidator implements Validator {

    public static final String EMAIL_ERROR_MESSAGE = "Email is required to make a Loan";
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String EMAIL_ERROR_CODE = "loan.id.invalid";

    @Autowired
    private EmailValidator emailValidator;

    @Override
    public boolean supports(Class<?> clazz) {

        return Loan.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Loan loan = (Loan)target;

        String email = loan.getEmail();

        if(email == null || !emailValidator.validate(loan.getEmail())) {
            errors.rejectValue(EMAIL_FIELD_NAME, EMAIL_ERROR_CODE, EMAIL_ERROR_MESSAGE);
        }

    }
}
