package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.loan.exceptions.EmailNotFoundException;
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

        loanService.borrowCopy(copy, "tcruz@thoughtworks.com");

        Loan loan = loanRepository.findByCopy(copy).get(0);
        Copy borrowedCopy = copyRepository.findOne(copy.getId());

        assertThat(borrowedCopy.getStatus(), is(CopyStatus.BORROWED));

        assertThat(loan, is(not(nullValue())));
        assertThat(loan.getStartDate(), is(not(nullValue())));
    }

    @Test(expected = EmailNotFoundException.class)
    public void shouldNotCreateLoanWithNoIdentify() {

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        assertThat(loanService.borrowCopy(copy, null), is(nullValue()));

    }

    @Test(expected = EmailNotFoundException.class)
    public void shouldNotCreateLoanWithInvalidEmail() {

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        assertThat(loanService.borrowCopy(copy, "test"), is(nullValue()));

    }

    @Test(expected = CopyIsNotAvailableException.class)
    public void shouldReturnNullWhenBookIsBorrowed() {

        copy = new CopyBuilder()
                .withBook(book)
                .withStatus(CopyStatus.BORROWED)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        loanService.borrowCopy(copy, "tcruz@thoughtworks.com");
    }
}