package com.thoughtworks.library;


import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.book.BookStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BookRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;


    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    //TODO Refatorar
    public static final String BOOK_1_TITLE_EXAMPLE = "BOOK 1 NAME EXAMPLE";
    public static final String BOOK_1_AUTHOR_EXAMPLE = "BOOK 1 AUTHOR EXAMPLE";
    public static final BookStatus BOOK_1_STATUS_EXAMPLE = BookStatus.LOANED;

    public static final String BOOK_2_TITLE_EXAMPLE = "BOOK 2 NAME EXAMPLE";
    public static final String BOOK_2_AUTHOR_EXAMPLE = "BOOK 2 AUTHOR EXAMPLE";
    public static final BookStatus BOOK_2_STATUS_EXAMPLE = BookStatus.AVAILABLE;

    public static final Integer BOOK_2_ID_EXAMPLE = 2;

    @Autowired
    BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

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
    public void shouldListAllBooks() throws Exception {

        try {

            ResultActions resultActions = mockMvc.perform(get("/books"));

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON))
                    .andExpect(jsonPath("$_embedded.books", hasSize(2)))
                    .andExpect(jsonPath("$_embedded.books[0].title", is(book1.getTitle())))
                    .andExpect(jsonPath("$_embedded.books[0].author", is(book1.getAuthor())))
                    .andExpect(jsonPath("$_embedded.books[0].status", is(book1.getStatus().name())))

                    .andExpect(jsonPath("$_embedded.books[1].title", is(book2.getTitle())))
                    .andExpect(jsonPath("$_embedded.books[1].author", is(book2.getAuthor())))
                    .andExpect(jsonPath("$_embedded.books[1].status", is(book2.getStatus().name())));


        }catch (Exception e){
            e.printStackTrace();;
        }
    }


}