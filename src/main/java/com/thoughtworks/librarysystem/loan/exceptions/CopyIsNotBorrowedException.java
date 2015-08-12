package com.thoughtworks.librarysystem.loan.exceptions;

public class CopyIsNotBorrowedException extends RuntimeException {

    public CopyIsNotBorrowedException(){

        super("Book requested couldn't return because it is already available.");
    }

}
