package com.thoughtworks.librarysystem.loan.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(){
        super("A user is required to make a Loan.");
    }

}
