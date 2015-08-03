package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.bookcopy.BookCopy;
import com.thoughtworks.library.bookcopy.BookCopyBuilder;
import com.thoughtworks.library.bookcopy.BookCopyRepository;
import com.thoughtworks.library.bookcopy.BookCopyStatus;
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
    BookCopyRepository bookCopyRepository;

    private BookCopy bookCopy;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();

        bookRepository.save(book);

    }

    @Test
    public void shouldLoanBookWhenItIsAvailable() throws  Exception{

        bookCopy = new BookCopyBuilder()
                        .withBook(book)
                        .build();

        bookCopyRepository.save(bookCopy);

        Loan loan = new Loan(bookCopy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("bookCopy", bookCopy);
        String loanJson = loadFixture("bookCopy.json", inputs);
        
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanJson))
                        .andExpect(status().isCreated());

        BookCopy borrowedBookCopy = bookCopyRepository.findOne(bookCopy.getId());

        Assert.assertThat(borrowedBookCopy.getStatus(), is(BookCopyStatus.BORROWED));

    }


    @Test
    public void shouldNotLoanBookWhenItIsAlreadyBorrowed() throws  Exception{

        bookCopy = new BookCopyBuilder()
                        .withBook(book)
                        .withStatus(BookCopyStatus.BORROWED)
                        .build();

        bookCopyRepository.save(bookCopy);

        Loan loan = new Loan(bookCopy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("bookCopy", bookCopy);
        String loanJson = loadFixture("bookCopy.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isPreconditionFailed());

        BookCopy borrowedBookCopy = bookCopyRepository.findOne(bookCopy.getId());


    }
}
