package com.thoughtworks.library.book;

public class BookBuilder {

    private Book instance = new Book();

    public BookBuilder withAuthor(String author){
        instance.setAuthor(author);
        return this;
    }

    public BookBuilder withTitle(String title){
        instance.setTitle(title);
        return this;
    }

    public BookBuilder withStatus(BookStatus status){
        instance.setStatus(status);
        return this;
    }

    public Book build(){
        return instance;
    }
}
