package com.thoughtworks.librarysystem.exceptions;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(){
        super("Library requested is not available.");
    }

}
