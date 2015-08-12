package com.thoughtworks.librarysystem.loan.exceptions;

public class LoanNotExistsException extends RuntimeException {

    public LoanNotExistsException(){
        super("This loan does not exists.");
    }

}
