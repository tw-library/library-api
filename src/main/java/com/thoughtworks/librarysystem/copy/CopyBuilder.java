package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class CopyBuilder {

    private Book book;
    private Library library;
    private CopyStatus status = CopyStatus.AVAILABLE;
    private String donator;

    public Copy build(){

        Copy copy = new Copy();

        copy.setBook(book);
        copy.setStatus(status);
        copy.setLibrary(library);
        copy.setDonator(donator);

        return copy;
    }
}
