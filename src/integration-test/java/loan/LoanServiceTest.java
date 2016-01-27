package loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.loan.Loan;
import com.thoughtworks.librarysystem.loan.LoanRepository;
import com.thoughtworks.librarysystem.loan.LoanService;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.loan.exceptions.UserNotFoundException;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class LoanServiceTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    private Copy copy;

    private Library library;

    private Book book;

    @Before
    public void setup() throws Exception {

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();
        bookRepository.save(book);

        library = new LibraryBuilder()
                .withName("belohorizonte")
                .withSlug("bh")
                .build();

        libraryRepository.save(library);

    }

    @Test
    public void shouldBorrowCopyWhenCopyIsAvailable() {

        copy = new CopyBuilder()
                        .withBook(book)
                        .withLibrary(library)
                        .build();

        copyRepository.save(copy);

        User user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userRepository.save(user);

        loanService.borrowCopy(copy.getId(), user.getEmail());

        Loan loan = loanRepository.findByCopy(copy).get(0);
        Copy borrowedCopy = copyRepository.findOne(copy.getId());

        assertThat(borrowedCopy.getStatus(), is(CopyStatus.BORROWED));

        assertThat(loan, is(not(nullValue())));
        assertThat(loan.getStartDate(), is(not(nullValue())));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotCreateLoanWithNoIdentify() {

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        assertThat(loanService.borrowCopy(copy.getId(), null), is(nullValue()));

    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotCreateLoanWithInvalidEmail() {

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        assertThat(loanService.borrowCopy(copy.getId(), null), is(nullValue()));

    }

    @Test(expected = CopyIsNotAvailableException.class)
    public void shouldReturnNullWhenBookIsBorrowed() {

        copy = new CopyBuilder()
                .withBook(book)
                .withStatus(CopyStatus.BORROWED)
                .withLibrary(library)
                .build();

        User user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withId(1).withName("Tulio").build();

        copyRepository.save(copy);

        loanService.borrowCopy(copy.getId(), user.getEmail());
    }
}