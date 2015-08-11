package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.exceptions.CopyNotAvailableException;
import com.thoughtworks.librarysystem.loan.exceptions.UserLoanNotIdentifyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;


    public Loan borrowCopy(Copy copy, String email) throws CopyNotAvailableException, UserLoanNotIdentifyException {

        if (copy.getStatus().equals(CopyStatus.BORROWED)) {
            throw new CopyNotAvailableException();
        }

        copy.setStatus(CopyStatus.BORROWED);
        copyRepository.save(copy);

        Loan loan = new LoanBuilder()
                .withCopy(copy)
                .withEmail(email)
                .build();

        return loanRepository.save(loan);

    }

}
