package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.commons.factories.BookFactory;
import com.thoughtworks.librarysystem.commons.factories.CopyFactory;
import com.thoughtworks.librarysystem.commons.factories.LibraryFactory;
import com.thoughtworks.librarysystem.commons.factories.UserFactory;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.user.User;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private Copy copyMultipleOne, copyMultipleTwo;

    private User userFirst, userSecond;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Library libraryBh = new LibraryFactory().createStardardLibrary();
        libraryRepository.save(libraryBh);
        Book bookMultiple = new BookFactory().createBookWithStandardIsbn();
        bookRepository.save(bookMultiple);
        copyMultipleOne = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleOne);
        copyMultipleTwo = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleTwo);

        userFirst = new UserFactory().createDefaultUser();
        userRepository.save(userFirst);

        userSecond = new UserFactory().createUserWithUserName("elayne");

        userRepository.save(userSecond);

    }

    @Test
    public void shouldReturnBookWhenItIsBorrowed() throws  Exception{

        Library library = new LibraryFactory().createLibrary("São Paulo","SP");
        libraryRepository.save(library);

        Book book = new BookFactory().createBookWithoutIsbn();
        bookRepository.save(book);

        Copy copyBorrowed = new CopyFactory().createCopyWithLibraryAndBook(library, book);
        copyRepository.save(copyBorrowed);

        User user = new UserFactory().createUserWithUserName("marcelo");
        userRepository.save(user);

        Loan loan = loanService.borrowCopy(library.getSlug(), book.getId(), user.getEmail());

        loanService.returnCopy(loan.getId());

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
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void shouldReturnCopyWithMultipleLoans() throws  Exception{

        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 2);

        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userFirst.getEmail());

        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 1);

        Loan loan = loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userSecond.getEmail());
        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 0);

        loanService.returnCopy(loan.getId());
        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 1);

    }


    @Test
    public void shouldBorrowAReturnedCopyWithMultipleLoans() throws  Exception {

        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 2);

        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userFirst.getEmail());

        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 1);

        Loan loan = loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userSecond.getEmail());
        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 0);

        loanService.returnCopy(loan.getId());
        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 1);

        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userSecond.getEmail());
        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 0);
        Assert.assertThat(copyMultipleOne.getStatus(), is(CopyStatus.BORROWED));

    }


    public void verifyNumberOfCopiesAvailables(String slug, Integer book_id, Integer expectedValue) throws Exception {
        String uri = "/copies/search/findDistinctCopiesByLibrarySlugAndBookIdAndStatus?slug=" + slug + "&book=" + book_id + "&status=" + CopyStatus.AVAILABLE;
        if (expectedValue > 0) {

            mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$_embedded.copies", hasSize(expectedValue)))
                    .andDo(print());

        } else {
            mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
    }


    private String mountUrlToPatchLoan(Loan loan) {
        return "/loans/" + loan.getId();
    }
}