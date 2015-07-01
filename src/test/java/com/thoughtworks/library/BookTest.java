package com.thoughtworks.library;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.thoughtworks.library.book.Book;
import junit.framework.TestCase;
import org.junit.Test;

public class BookTest {

    @Test
    public void shouldTestIfBookExist() {

        Book bookOne = new Book();

        assertThat(bookOne, notNullValue(Book.class));
    }

}