package com.thoughtworks.librarysystem.exceptions;

public class CopyNotAvailableException extends RuntimeException {


    public CopyNotAvailableException(){
        super("Copy of book requested is not available.");
    }

}
