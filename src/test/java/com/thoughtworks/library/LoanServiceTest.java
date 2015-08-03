package com.thoughtworks.library;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookBuilder;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.bookcopy.BookCopy;
import com.thoughtworks.library.bookcopy.BookCopyBuilder;
import com.thoughtworks.library.bookcopy.BookCopyRepository;
import com.thoughtworks.library.bookcopy.BookCopyStatus;
import com.thoughtworks.library.exceptions.BookCopyNotAvailableException;
import com.thoughtworks.library.loan.LoanService;
import com.thoughtworks.library.loan.Loan;
import com.thoughtworks.library.loan.LoanRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class LoanServiceTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private LoanService loanService;

    private BookCopy bookCopy;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Before
    public void setup() throws Exception {

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();
        bookRepository.save(book);

    }


    @Test
    public void shouldBorrowBookCopyWhenBookCopyIsAvailable() {

        bookCopy = new BookCopyBuilder()
                        .withBook(book)
                        .build();

        bookCopyRepository.save(bookCopy);

        loanService.borrowBookCopy(bookCopy);

        Loan loan = loanRepository.findByBookCopy(bookCopy).get(0);
        BookCopy borrowedBookCopy = bookCopyRepository.findOne(bookCopy.getId());

        assertThat(borrowedBookCopy.getStatus(), is(BookCopyStatus.BORROWED));

        assertThat(loan, is(not(nullValue())));
        assertThat(loan.getStartDate(), is(not(nullValue())));
    }

    @Test(expected = BookCopyNotAvailableException.class)
    public void shouldReturnNullWhenBookIsBorrowed() {

        bookCopy = new BookCopyBuilder()
                .withBook(book)
                .withStatus(BookCopyStatus.BORROWED)
                .build();

        bookCopyRepository.save(bookCopy);

        loanService.borrowBookCopy(bookCopy);
    }
}