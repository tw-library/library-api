package com.thoughtworks.library.book;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class BookBuilder {

    private String author = "Guimares Sanches";
    private String title = "Minha Casa";
    private BookStatus status = BookStatus.AVAILABLE;

    public Book build(){
        Book book = new Book();

        book.setAuthor(author);
        book.setStatus(status);
        book.setTitle(title);

        return book;
    }
}
