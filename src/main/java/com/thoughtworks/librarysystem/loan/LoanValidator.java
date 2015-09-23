package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.commons.EmailValidator;
import com.thoughtworks.librarysystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
public class LoanValidator implements Validator {

    public static final String USER_ERROR_MESSAGE = "User with valid email is required to make a Loan";
    public static final String USER_FIELD_NAME = "user";
    public static final String USER_ERROR_CODE = "loan.id.invalid";

    @Autowired
    private EmailValidator emailValidator;

    @Override
    public boolean supports(Class<?> clazz) {

        return Loan.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Loan loan = (Loan)target;

        if(loan == null || !emailValidator.validate(loan.getEmail())) {
            errors.rejectValue(USER_FIELD_NAME, USER_ERROR_CODE, USER_ERROR_MESSAGE);
        }

    }
}
