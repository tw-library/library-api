package com.thoughtworks.librarysystem.copy;


import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.loan.Loan;
import com.thoughtworks.librarysystem.loan.LoanService;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
public class CopyWithLastLoanTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    CopyRepository copyRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    private LoanService loanService;

    private Book book;
    private Copy copy;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        book = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off a plant unit right with Plant Parts from Pebble Plus. Clear text and large, " +
                        "striking photographs reveal each part of a plant and the role it plays in keeping plants" +
                        " thriving. Clear diagrams enhance the experience of exploring each part. So plant the " +
                        "seeds for a love of life science with Plant Parts!\"")
                .withIsbn13(9780736896191L)
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        bookRepository.save(book);

        Library library = new LibraryBuilder()
                .withName("belohorizonte")
                .withSlug("bh")
                .build();

        libraryRepository.save(library);

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .withDonator("Tulio Cruz")
                .build();

        copyRepository.save(copy);
    }

    @Test
    public void shouldListLastLoanByACopy() throws Exception {

        User user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userRepository.save(user);

        Loan loan = loanService.borrowCopy(copy.getId(), user.getEmail());

        loanService.returnCopy(loan.getId());

        Loan anotherLoan = loanService.borrowCopy(copy.getId(), user.getEmail());

        mockMvc.perform(get(mountUrlToGetCopy(copy)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.lastLoan.id", is(anotherLoan.getId())));

    }

    private String mountUrlToGetCopy(Copy copy) {
        return "/copies/" + copy.getId();
    }


}
