package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotBorrowedException;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.loan.exceptions.LoanNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;

    public Loan borrowCopy(Copy copy, String email) throws CopyIsNotAvailableException {

        if (copy.getStatus().equals(CopyStatus.BORROWED)) {
            throw new CopyIsNotAvailableException();
        }

        copy.setStatus(CopyStatus.BORROWED);
        copyRepository.save(copy);

        Loan loan = new LoanBuilder()
                .withCopy(copy)
                .withEmail(email)
                .build();

        return loanRepository.save(loan);

    }

    public Loan returnCopy(Loan loan) throws CopyIsNotBorrowedException, LoanNotExistsException {

        loan = loanRepository.findOne(loan.getId());

        if(loan == null){
            throw new LoanNotExistsException();
        }

        Copy copy = loan.getCopy();

        if (copy.getStatus().equals(CopyStatus.AVAILABLE)) {
            throw new CopyIsNotBorrowedException();
        }

        copy.setStatus(CopyStatus.AVAILABLE);
        copyRepository.save(copy);

        if(loan.getEndDate() == null) {
            loan.setEndDate(new Date(System.currentTimeMillis()));
            loanRepository.save(loan);
        }

        return loan;

    }

}
