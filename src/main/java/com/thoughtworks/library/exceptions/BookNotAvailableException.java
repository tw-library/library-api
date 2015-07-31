package com.thoughtworks.library.exceptions;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(){
        super("Book requested is not available.");
    }

}
