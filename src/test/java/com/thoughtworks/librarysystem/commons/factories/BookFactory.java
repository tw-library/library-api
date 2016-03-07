package com.thoughtworks.librarysystem.commons.factories;


import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;

/**
 * Created by eferreir on 2/8/16.
 */
public class BookFactory {


    public Book createBookWithStandardIsbn() {

        long sameIsbn = 9780736896191L;

        Book book = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off a plant unit right with Plant Parts from Pebble Plus. Clear text and large, " +
                        "striking photographs reveal each part of a plant and the role it plays in keeping plants" +
                        " thriving. Clear diagrams enhance the experience of exploring each part. So plant the " +
                        "seeds for a love of life science with Plant Parts!\"")
                .withIsbn13(sameIsbn)
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        return book;
    }

    public Book createBookWithIsbn(Long isbn) {
        Book book = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off a plant unit right with Plant Parts from Pebble Plus. Clear text and large, " +
                        "striking photographs reveal each part of a plant and the role it plays in keeping plants" +
                        " thriving. Clear diagrams enhance the experience of exploring each part. So plant the " +
                        "seeds for a love of life science with Plant Parts!\"")
                .withIsbn13(isbn)
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();
        return book;
    }

    public Book createBookWithoutIsbn() {
        Book book = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off a plant unit right with Plant Parts from Pebble Plus. Clear text and large, " +
                        "striking photographs reveal each part of a plant and the role it plays in keeping plants" +
                        " thriving. Clear diagrams enhance the experience of exploring each part. So plant the " +
                        "seeds for a love of life science with Plant Parts")
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        return book;
    }

}
