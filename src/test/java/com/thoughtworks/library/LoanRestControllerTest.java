package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.book.BookStatus;
import com.thoughtworks.library.loan.Loan;
import org.junit.Assert;
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

import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class LoanRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void shouldLoanBookWhenItIsAvailable() throws  Exception{

        book = new BookBuilder().build();

        bookRepository.save(book);

        Loan loan = new Loan(book);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("book", book);
        String loanJson = loadFixture("book.json", inputs);
        
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanJson))
                        .andExpect(status().isCreated());

        Book borrowedBook = bookRepository.findOne(book.getId());

        Assert.assertThat(borrowedBook.getStatus(), is(BookStatus.BORROWED));

    }


    @Test
    public void shouldNotLoanBookWhenItIsAlreadyBorrowed() throws  Exception{

        book = new BookBuilder()
                        .withStatus(BookStatus.BORROWED)
                        .build();

        bookRepository.save(book);

        Loan loan = new Loan(book);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("book", book);
        String loanJson = loadFixture("book.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isPreconditionFailed());

        Book borrowedBook = bookRepository.findOne(book.getId());


    }
}
