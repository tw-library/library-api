package com.thoughtworks.librarysystem.book;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class BookBuilder {

    private String title;
    private String author;
    private String subtitle;
    private String description;
    private Long isbn13;
    private String publisher;
    private String publicationDate;
    private Integer numberOfPages;
    private String donator;
    private String imageUrl;

    public Book build(){

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setSubtitle(subtitle);
        book.setDescription(description);
        book.setIsbn13(isbn13);
        book.setPublisher(publisher);
        book.setPublicationDate(publicationDate);
        book.setNumberOfPages(numberOfPages);
        book.setDonator(donator);
        book.setImageUrl(imageUrl);

        return book;
    }
}
