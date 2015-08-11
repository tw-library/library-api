package com.thoughtworks.librarysystem.commons;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseError {

    private String message;
    private String field;

    public ResponseError(String message) {
        this.message = message;
    }

    public ResponseError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}
