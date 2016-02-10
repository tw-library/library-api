package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
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
    private Copy copyFirst, copySecond, copyThird, copyFourth;
    private Book bookFirst, bookSecond, bookThird;
    Loan loanFirst,loanSecond;
    String loanJson;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        Library libraryFirst = new LibraryBuilder()
                .withName("Belo Horizonte")
                .withSlug("BH")
                .build();

        Library librarySecond = new LibraryBuilder()
                .withName("Porto Alegre")
                .withSlug("POA")
                .build();

        libraryRepository.save(libraryFirst);
        libraryRepository.save(librarySecond);

        userFirst = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userSecond = new UserBuilder()
                .withEmail("eferreir@thoughtworks.com")
                .withName("Elayne")
                .build();

        userThird = new UserBuilder()
                .withEmail("fran@thoughtworks.com")
                .withName("Francielle")
                .build();

        userRepository.save(userFirst);
        userRepository.save(userSecond);
        userRepository.save(userThird);

        bookFirst = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off")
                .withIsbn13(9780736896191L)
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        bookSecond = new BookBuilder()
                .withTitle("Second Flowers")
                .withAuthor("Second Vijaya Khisty Bodach")
                .withSubtitle("Second")
                .withDescription("Description of the second book")
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                        "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                        "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        bookRepository.save(bookFirst);
        bookRepository.save(bookSecond);

        copyFirst = new CopyBuilder()
                .withBook(bookFirst)
                .withLibrary(libraryFirst)
                .build();

        copySecond = new CopyBuilder()
                .withBook(bookFirst)
                .withLibrary(libraryFirst)
                .build();

        copyThird = new CopyBuilder()
                .withBook(bookSecond)
                .withLibrary(libraryFirst)
                .build();

        copyFourth = new CopyBuilder()
                .withBook(bookSecond)
                .withLibrary(librarySecond)
                .build();


        copyRepository.save(copyFirst);
        copyRepository.save(copySecond);
        copyRepository.save(copyThird);
        copyRepository.save(copyFourth);

        loanFirst = new LoanBuilder()
                .build();

        loanSecond = new LoanBuilder()
                .build();

    }

    @Test
    public void shouldListOnePendingLoanBorrowedByOneUser() throws Exception {
        loanFirst = loanService.borrowCopy(bookFirst.getId(), userFirst.getEmail());
        String email = loanFirst.getEmail();

        Integer book_id = copyFirst.getBook().getId();
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
        loanFirst = loanService.borrowCopy(bookSecond.getId(), userSecond.getEmail());
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
        loanFirst = loanService.borrowCopy(copyFirst.getId(), userSecond.getEmail());
        loanSecond = loanService.borrowCopy(copySecond.getId(), userFirst.getEmail());

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


}
