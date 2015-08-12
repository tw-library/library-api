package com.thoughtworks.librarysystem.loan.exceptions;

public class CopyIsNotAvailableException extends RuntimeException {

    public CopyIsNotAvailableException(){
        super("Copy of book requested is not available.");
    }

}
