package com.thoughtworks.librarysystem.loan.exceptions;

public class UserLoanNotIdentifyException extends RuntimeException {

    public UserLoanNotIdentifyException(){
        super("To make Loan of book, the field email is required.");
    }
}

