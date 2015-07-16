package com.thoughtworks.library.loan;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.book.BookRepository;
import com.thoughtworks.library.book.BookStatus;
import com.thoughtworks.library.exceptions.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;


    public void borrowBook(Book book) throws BookNotAvailableException {

        if (book.getStatus().equals(BookStatus.BORROWED)) throw new BookNotAvailableException();

        book.setStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        Loan loan = new Loan(book);
        loanRepository.save(loan);
    }

}
