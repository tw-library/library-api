package com.thoughtworks.librarysystem.loan.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(){
        super("Email is required to make a Loan.");
    }

}
