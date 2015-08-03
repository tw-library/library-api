package com.thoughtworks.library.loan;

import com.thoughtworks.library.bookcopy.BookCopy;
import com.thoughtworks.library.bookcopy.BookCopyRepository;
import com.thoughtworks.library.bookcopy.BookCopyStatus;
import com.thoughtworks.library.exceptions.BookCopyNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;


    public void borrowBookCopy(BookCopy bookCopy) throws BookCopyNotAvailableException {

        if (bookCopy.getStatus().equals(BookCopyStatus.BORROWED)) throw new BookCopyNotAvailableException();

        bookCopy.setStatus(BookCopyStatus.BORROWED);
        bookCopyRepository.save(bookCopy);

        Loan loan = new Loan(bookCopy);
        loanRepository.save(loan);
    }

}
