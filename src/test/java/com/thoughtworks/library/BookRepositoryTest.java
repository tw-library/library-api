package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.book.BookStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext
@Transactional
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @Before
    public void setUp() {

        book1 = new BookBuilder()
                        .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                        .withTitle("BOOK 1 NAME EXAMPLE")
                        .withStatus(BookStatus.BORROWED)
                        .build();
        bookRepository.save(book1);


        book2 = new BookBuilder()
                .withAuthor("BOOK 2 AUTHOR EXAMPLE")
                .withTitle("BOOK 2 NAME EXAMPLE")
                .withStatus(BookStatus.AVAILABLE)
                .build();
        bookRepository.save(book2);

    }

    @Test
    public void shouldListAllBooks(){

        List<Book> books = new ArrayList<>();

        for (Book book : bookRepository.findAll()) {
            books.add(book);
        }

        assertThat(books.size(), equalTo(2));

        Book book = books.get(0);

        assertThat(book.getTitle(), equalTo(book1.getTitle()));
        assertThat(book.getAuthor(), equalTo(book1.getAuthor()));
        assertThat(book.getStatus(), equalTo(book1.getStatus()));

        book = books.get(1);
        assertThat(book.getTitle(), equalTo(book2.getTitle()));
        assertThat(book.getAuthor(), equalTo(book2.getAuthor()));
        assertThat(book.getStatus(), equalTo(book2.getStatus()));

    }
}
