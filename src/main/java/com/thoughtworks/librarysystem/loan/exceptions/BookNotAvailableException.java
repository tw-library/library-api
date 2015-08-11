package com.thoughtworks.librarysystem.loan.exceptions;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(){
        super("Library requested is not available.");
    }

}
