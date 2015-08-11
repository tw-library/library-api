package com.thoughtworks.librarysystem.loan.exceptions;

public class CopyNotAvailableException extends RuntimeException {

    public CopyNotAvailableException(){
        super("Copy of book requested is not available.");
    }

}
