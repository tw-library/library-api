package com.thoughtworks.library;


import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.bookcopy.BookCopyStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class BookRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        book1 = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();
        bookRepository.save(book1);


        book2 = new BookBuilder()
                .withAuthor("BOOK 2 AUTHOR EXAMPLE")
                .withTitle("BOOK 2 NAME EXAMPLE")
                .build();
        bookRepository.save(book2);

    }

    @Test
    public void shouldListAllBooks() throws Exception {

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$_embedded.books", hasSize(2)))
                .andExpect(jsonPath("$_embedded.books[0].title", is(book1.getTitle())))
                .andExpect(jsonPath("$_embedded.books[0].author", is(book1.getAuthor())))

                .andExpect(jsonPath("$_embedded.books[1].title", is(book2.getTitle())))
                .andExpect(jsonPath("$_embedded.books[1].author", is(book2.getAuthor())));
    }

}