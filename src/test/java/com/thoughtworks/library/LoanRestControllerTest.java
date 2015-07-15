package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.book.BookStatus;
import com.thoughtworks.library.loan.Loan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class LoanRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .withStatus(BookStatus.BORROWED)
                .build();

        bookRepository.save(book);
    }

    @Test
    public void shouldMakeBookLoan() throws  Exception{

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStartDate(new Date(System.currentTimeMillis()));
        loan.setEndDate(new Date(System.currentTimeMillis()));

        //TODO I know that I need to refactor this and figure out how I can make a request passing a book entity
        String loanJson = "{ \"book\":\"/books/1\"}";
        
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanJson))
                        .andExpect(status().isCreated());
    }

}
