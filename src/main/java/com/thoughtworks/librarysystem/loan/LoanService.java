package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.exceptions.CopyNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;


    public void borrowCopy(Copy bookCopy) throws CopyNotAvailableException {

        if (bookCopy.getStatus().equals(CopyStatus.BORROWED)) throw new CopyNotAvailableException();

        bookCopy.setStatus(CopyStatus.BORROWED);
        copyRepository.save(bookCopy);

        Loan loan = new Loan(bookCopy);
        loanRepository.save(loan);
    }

}
