package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
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
public class BorrowBookRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    CopyRepository copyRepository;

    private Copy copy;

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

        copy = new CopyBuilder()
                        .withBook(book)
                        .build();

        copyRepository.save(copy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", "tcruz@thoughtworks.com");

        String loanJson = loadFixture("loan.json", inputs);
        
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanJson))
                        .andExpect(status().isCreated());

        Copy borrowedBookCopy = copyRepository.findOne(copy.getId());

        Assert.assertThat(borrowedBookCopy.getStatus(), is(CopyStatus.BORROWED));

    }

    @Test
    public void shouldNotLoanBookWhenItIsAlreadyBorrowed() throws  Exception{

        copy = new CopyBuilder()
                .withBook(book)
                .withStatus(CopyStatus.BORROWED)
                .build();

        copyRepository.save(copy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", "tcruz@thoughtworks.com");

        String loanJson = loadFixture("loan.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isPreconditionFailed());

    }

    @Test
    public void shouldNotLoanBookWhenItIsAvailableButYouDoNotIdentifyYourself() throws  Exception{

        copy = new CopyBuilder()
                .withBook(book)
                .build();

        copyRepository.save(copy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", "");


        String loanJson = loadFixture("loan.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isPreconditionFailed());
    }
}