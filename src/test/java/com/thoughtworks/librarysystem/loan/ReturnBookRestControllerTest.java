package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class ReturnBookRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;


    @Autowired
    private LoanService loanService;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnBookWhenItIsBorrowed() throws  Exception{

        Copy copyBorrowed;
        Loan loan;
        Library library = new LibraryBuilder()
                .withName("Teste")
                .withSlug("T")
                .build();

        libraryRepository.save(library);

        Book book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();

        bookRepository.save(book);

        copyBorrowed = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copyBorrowed);

        User user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userRepository.save(user);


        loan = loanService.borrowCopy(copyBorrowed.getId(), user.getEmail());

        mockMvc.perform(patch(mountUrlToPatchLoan(loan))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                            .andExpect(status().isNoContent());

        Copy borrowedBookCopy = copyRepository.findOne(copyBorrowed.getId());

        Assert.assertThat(borrowedBookCopy.getStatus(), is(CopyStatus.AVAILABLE));
    }

    @Test
    public void shouldNotReturnBookWhenLoanDoesNotExist() throws  Exception{

        Loan invalidLoan = new LoanBuilder()
                                .withId(0)
                                .build();

        mockMvc.perform(patch(mountUrlToPatchLoan(invalidLoan))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isPreconditionRequired());

    }

    private String mountUrlToPatchLoan(Loan loan) {
        return "/loans/" + loan.getId();
    }
}