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
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.loan.exceptions.UserNotFoundException;
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

import java.util.HashMap;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class BorrowBookRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    CopyRepository copyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    private LoanService loanService;


    private Copy copy;
    private User user, userFirst, userSecond;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        book = new BookFactory().createBookWithoutIsbn();

        bookRepository.save(book);
    }

    @Test
    public void shouldLoanBookWhenItIsAvailable() throws Exception {

        Library library = new LibraryFactory().createLibrary("Teste", "T");
        libraryRepository.save(library);

        copy = new CopyFactory().createCopyWithLibraryAndBook(library, book);
        copyRepository.save(copy);

        user = new UserFactory().createUserWithUserName("diego");
        userRepository.save(user);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", user.getEmail());

        String loanJson = loadFixture("borrow_a_book.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isCreated());

        Copy borrowedBookCopy = copyRepository.findOne(copy.getId());

        Assert.assertThat(borrowedBookCopy.getStatus(), is(CopyStatus.BORROWED));

    }

    @Test(expected = CopyIsNotAvailableException.class)
    public void shouldNotLoanBookWhenItIsAlreadyBorrowed() throws Exception {
        Library libraryBh = new LibraryFactory().createStardardLibrary();
        libraryRepository.save(libraryBh);
        Book bookMultiple = new BookFactory().createBookWithStandardIsbn();
        bookRepository.save(bookMultiple);
        Copy copyMultipleOne = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleOne);
        Copy copyMultipleTwo = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleTwo);

        userFirst = new UserFactory().createUserWithUserName("pedro");
        userRepository.save(userFirst);

        userSecond = new UserFactory().createUserWithUserName("joao");
        userRepository.save(userSecond);

        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userFirst.getEmail());
        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userFirst.getEmail());
        verifyNumberOfCopiesAvailables(libraryBh.getSlug(), bookMultiple.getId(), 0);
        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userSecond.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotLoanBookWhenItIsAvailableButYouDoNotIdentifyYourself() throws Exception {

        Library library = new LibraryFactory().createLibrary("São Paulo", "SP");
        libraryRepository.save(library);

        copy = new CopyFactory().createCopyWithLibraryAndBook(library, book);

        user = new UserFactory().createUserWithUserName("naoexiste");

        copyRepository.save(copy);

        loanService.borrowCopy(library.getSlug(), book.getId(), user.getEmail());
    }


    @Test(expected = CopyIsNotAvailableException.class)
    public void shouldNotLoanBookWhenItDoesNotHaveAnyCopy() throws Exception {

        Library libraryPOA = new LibraryFactory().createLibrary("Porto Alegre", "POA");
        libraryRepository.save(libraryPOA);

        user = new UserFactory().createUserWithUserName("tulio");
        userRepository.save(user);

        Book book = new BookFactory().createBookWithoutIsbn();
        bookRepository.save(book);

        loanService.borrowCopy(libraryPOA.getSlug(), this.book.getId(), user.getEmail());
    }


    @Test
    public void shouldLoanAvailableBookWhenThereAreOtherCopiesBorrowed() throws Exception {

        Library libraryBh = new LibraryFactory().createStardardLibrary();
        libraryRepository.save(libraryBh);
        Book bookMultiple = new BookFactory().createBookWithStandardIsbn();
        bookRepository.save(bookMultiple);
        Copy copyMultipleOne = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleOne);
        Copy copyMultipleTwo = new CopyFactory().createCopyWithLibraryAndBook(libraryBh, bookMultiple);
        copyRepository.save(copyMultipleTwo);

        userFirst = new UserFactory().createDefaultUser();
        userRepository.save(userFirst);

        userSecond = new UserFactory().createUserWithUserName("elayne");

        userRepository.save(userSecond);

        verifyNumberOfCopiesAvailables(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), 2);

        loanService.borrowCopy(copyMultipleOne.getLibrary().getSlug(), copyMultipleOne.getBook().getId(), userFirst.getEmail());

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
                    .andExpect(status().isOk())
                    .andExpect(content().string("{ }"))
                    .andDo(print());
        }
    }


}