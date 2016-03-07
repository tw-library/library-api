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
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by eferreir on 2/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class LoanRepositoryTest extends ApplicationTestBase {

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

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    private User userFirst, userSecond, userThird;
    private Copy copyRepeatedFirst, copyRepeatedSecond, copyRepeatedThird, copyRepeatedFourth, copyThird, copyFourth;
    private Book bookFirst, bookSecond, bookThird;
    Loan loanFirst,loanSecond;
    String loanJson;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Library libraryFirst = new LibraryFactory().createStardardLibrary();
        Library librarySecond = new LibraryFactory().createLibrary("Porto Alegre", "POA");

        libraryRepository.save(libraryFirst);
        libraryRepository.save(librarySecond);

        userFirst = new UserFactory().createUserWithUserName("tulio");
        userSecond = new UserFactory().createUserWithUserName("elayne");
        userThird = new UserFactory().createUserWithUserName("francielle");

        userRepository.save(userFirst);
        userRepository.save(userSecond);
        userRepository.save(userThird);

        bookFirst = new BookFactory().createBookWithStandardIsbn();
        bookSecond = new BookFactory().createBookWithoutIsbn();

        bookRepository.save(bookFirst);
        bookRepository.save(bookSecond);

        copyRepeatedFirst = new CopyFactory().createCopyWithLibraryAndBook(libraryFirst, bookFirst);
        copyRepeatedSecond =new CopyFactory().createCopyWithLibraryAndBook(libraryFirst, bookFirst);
        copyRepeatedThird =new CopyFactory().createCopyWithLibraryAndBook(libraryFirst, bookFirst);
        copyRepeatedFourth =new CopyFactory().createCopyWithLibraryAndBook(libraryFirst, bookFirst);


        copyThird = new CopyFactory().createCopyWithLibraryAndBook(libraryFirst, bookSecond);
        copyFourth = new CopyFactory().createCopyWithLibraryAndBook(librarySecond, bookSecond);

        loanFirst = new LoanBuilder()
                .build();

        loanSecond = new LoanBuilder()
                .build();


        copyRepeatedSecond.setLastLoan(loanFirst);

        copyRepository.save(copyRepeatedFirst);
        copyRepository.save(copyRepeatedSecond);
        copyRepository.save(copyRepeatedThird);
        copyRepository.save(copyRepeatedFourth);
        copyRepository.save(copyThird);
        copyRepository.save(copyFourth);


    }

    @Test
    public void shouldListOnePendingLoanBorrowedByOneUser() throws Exception {
        loanFirst = loanService.borrowCopy(copyRepeatedFirst.getLibrary().getSlug(), copyRepeatedFirst.getBook().getId(), userFirst.getEmail());
        String email = loanFirst.getEmail();

        Integer book_id = copyRepeatedFirst.getBook().getId();
        String uri = "/loans/search/findByEndDateIsNullAndCopyLibrarySlugAndCopyBookId?slug=BH&book=" + book_id;
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$_embedded.loans[0].id", is(loanFirst.getId())))
                .andExpect(jsonPath("$_embedded.loans[0].startDate", is(loanFirst.getStartDate().toString())))
                .andExpect(jsonPath("$_embedded.loans[0].endDate", is(loanFirst.getEndDate())))
                .andExpect(jsonPath("$_embedded.loans[0].email", is(loanFirst.getEmail())))
                .andExpect(jsonPath("$_embedded.loans[0]._embedded.copy.id", is(loanFirst.getCopy().getId())))
                .andDo(print());
    }

    @Test
    public void shouldListZeroPendingLoanOfAReturnedBook() throws Exception {
        loanFirst = loanService.borrowCopy(copyRepeatedSecond.getLibrary().getSlug(), copyRepeatedSecond.getBook().getId(), userSecond.getEmail());
        loanService.returnCopy(loanFirst.getId());
        String email = loanFirst.getEmail();
        Integer book_id = loanFirst.getCopy().getBook().getId();
        String uri = "/loans/search/findByEndDateIsNullAndCopyLibrarySlugAndCopyBookId?slug=BH&book=" + book_id;
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{ }"))
                 .andDo(print());
    }

    @Test
    public void shouldListTwoPendingLoanBooksBorrowedByTwoUsers() throws Exception {
        loanFirst = loanService.borrowCopy(copyRepeatedFirst.getLibrary().getSlug(), copyRepeatedFirst.getBook().getId(), userSecond.getEmail());
        loanSecond = loanService.borrowCopy(copyRepeatedSecond.getLibrary().getSlug(), copyRepeatedSecond.getBook().getId(), userFirst.getEmail());

        Integer book_id = loanFirst.getCopy().getBook().getId();
        String uri = "/loans/search/findByEndDateIsNullAndCopyLibrarySlugAndCopyBookId?slug=BH&book=" + book_id;
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$_embedded.loans", hasSize(2)))
                .andExpect(jsonPath("$_embedded.loans[0].email", is(loanFirst.getEmail())))
                .andExpect(jsonPath("$_embedded.loans[1].email",is(loanSecond.getEmail())))
                .andExpect(jsonPath("$_embedded.loans[0].endDate", is(loanFirst.getEndDate())))
                .andExpect(jsonPath("$_embedded.loans[1].endDate", is(loanSecond.getEndDate())))
                .andExpect(jsonPath("$_embedded.loans[0]._embedded.copy.status", is(loanFirst.getCopy().getStatus().toString())))
                .andExpect(jsonPath("$_embedded.loans[1]._embedded.copy.status", is(loanSecond.getCopy().getStatus().toString())))
                .andDo(print());
    }


    @Test
    public void shouldListOnePendingForTheUserSecond() throws Exception {
        loanFirst = loanService.borrowCopy(copyRepeatedThird.getLibrary().getSlug(), copyRepeatedThird.getBook().getId(), userSecond.getEmail());
        Integer book_id = loanFirst.getCopy().getBook().getId();
        String uri = "/loans/search/countByEndDateIsNullAndCopyLibrarySlugAndCopyBookIdAndUserEmail?slug=BH&book=" + book_id+ "&email=" + userSecond.getEmail();
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());
    }

    @Test
    public void shouldListAnyPendingForTheUserThird() throws Exception {
        loanFirst = loanService.borrowCopy(copyRepeatedSecond.getLibrary().getSlug(), copyRepeatedSecond.getBook().getId(), userThird.getEmail());
        loanService.returnCopy(loanFirst.getId());
        Integer book_id = loanFirst.getCopy().getBook().getId();

        String uri = "/loans/search/countByEndDateIsNullAndCopyLibrarySlugAndCopyBookIdAndUserEmail?slug=BH&book=" + book_id+ "&email=" + userThird.getEmail();
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"))
                .andDo(print());
    }


}
