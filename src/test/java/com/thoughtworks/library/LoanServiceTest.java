package com.thoughtworks.library;

import com.thoughtworks.library.book.*;
import com.thoughtworks.library.exceptions.BookNotAvailableException;
import com.thoughtworks.library.loan.LoanService;
import com.thoughtworks.library.loan.Loan;
import com.thoughtworks.library.loan.LoanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class LoanServiceTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanService loanService;

    private Book book;

    @Test
    public void shouldBorrowBookWhenBookIsAvailable() {

        book = new BookBuilder().build();

        bookRepository.save(book);

        loanService.borrowBook(book);

        Loan loan = loanRepository.findByBook(book).get(0);
        Book borrowedBook = bookRepository.findOne(book.getId());

        assertThat(borrowedBook.getStatus(), is(BookStatus.BORROWED));

        assertThat(loan, is(not(nullValue())));
        assertThat(loan.getStartDate(), is(not(nullValue())));
    }

    @Test(expected = BookNotAvailableException.class)
    public void shouldReturnNullWhenBookIsBorrowed() {

        book = new BookBuilder()
                .withStatus(BookStatus.BORROWED)
                .build();

        bookRepository.save(book);

        loanService.borrowBook(book);
    }
}