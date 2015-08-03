package com.thoughtworks.library.exceptions;

public class BookCopyNotAvailableException extends RuntimeException {


    public BookCopyNotAvailableException(){
        super("Copy of book requested is not available.");
    }

}
