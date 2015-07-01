package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.thoughtworks.library.book.BookStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BookRepositoryTest{

    public static final String BOOK_1_TITLE_EXAMPLE = "BOOK 1 NAME EXAMPLE";
    public static final String BOOK_1_AUTHOR_EXAMPLE = "BOOK 1 AUTHOR EXAMPLE";
    public static final BookStatus BOOK_1_STATUS_EXAMPLE = BookStatus.LOANED;

    public static final String BOOK_2_TITLE_EXAMPLE = "BOOK 2 NAME EXAMPLE";
    public static final String BOOK_2_AUTHOR_EXAMPLE = "BOOK 2 AUTHOR EXAMPLE";
    public static final BookStatus BOOK_2_STATUS_EXAMPLE = BookStatus.AVAILABLE;

    @Autowired
    BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @Before
    public void setUp() {

        book1 = new Book();
        book1.setTitle(BOOK_1_TITLE_EXAMPLE);
        book1.setAuthor(BOOK_1_AUTHOR_EXAMPLE);
        book1.setStatus(BOOK_1_STATUS_EXAMPLE);
        bookRepository.save(book1);

        book2 = new Book();
        book2.setTitle(BOOK_2_TITLE_EXAMPLE);
        book2.setAuthor(BOOK_2_AUTHOR_EXAMPLE);
        book2.setStatus(BOOK_2_STATUS_EXAMPLE);
        bookRepository.save(book2);
    }

    @Test
    public void shouldListAllBooks(){

        List<Book> bookList = new ArrayList<>();

        for (Book book : bookRepository.findAll())  bookList.add(book);

        assertThat(bookList.size(), equalTo(2));

        Book book = bookList.get(0);

        assertThat(book.getTitle(), equalTo(BOOK_1_TITLE_EXAMPLE));
        assertThat(book.getAuthor(), equalTo(BOOK_1_AUTHOR_EXAMPLE));
        assertThat(book.getStatus(), equalTo(BOOK_1_STATUS_EXAMPLE));

        book = bookList.get(1);
        assertThat(book.getTitle(), equalTo(BOOK_2_TITLE_EXAMPLE));
        assertThat(book.getAuthor(), equalTo(BOOK_2_AUTHOR_EXAMPLE));
        assertThat(book.getStatus(), equalTo(BOOK_2_STATUS_EXAMPLE));

    }
}
