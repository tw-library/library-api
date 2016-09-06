package com.thoughtworks.librarysystem.commons.factories;


import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.library.Library;

/**
 * Created by eferreir on 2/8/16.
 */
public class CopyFactory {

    public Copy createStandardCopyWithSameIsbnAndLibraryAndBorrowed() {

        Book book = new BookFactory().createBookWithStandardIsbn();
        Library library = new LibraryFactory().createStardardLibrary();

        Copy copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .withStatus(CopyStatus.BORROWED)
                .build();
        return copy;
    }

    public Copy createStandardCopyWithSameIsbnAndLibrary() {

        Book book = new BookFactory().createBookWithStandardIsbn();
        Library library = new LibraryFactory().createStardardLibrary();

        Copy copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();
        return copy;
    }


    public Copy createStandardCopyWithSpecificLibrary(Library library) {
        Book book = new BookFactory().createBookWithStandardIsbn();

        Copy copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();
        return copy;
    }


    public Copy createCopyWithLibraryAndBook(Library library, Book book) {

        Copy copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();
        return copy;
    }

}
