package com.thoughtworks.library.bookcopy;

import com.thoughtworks.library.book.Book;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class BookCopyBuilder {

    private Book book;
    private BookCopyStatus status = BookCopyStatus.AVAILABLE;

    public BookCopy build(){

        BookCopy bookCopy = new BookCopy();

        bookCopy.setBook(book);
        bookCopy.setStatus(status);

        return bookCopy;
    }
}
