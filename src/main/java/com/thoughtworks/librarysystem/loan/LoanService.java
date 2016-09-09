package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.commons.EmailValidator;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.exceptions.*;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailValidator emailValidator;

    public Loan borrowCopy(String slug, Integer bookId, String email) throws CopyIsNotAvailableException {

        List <Copy> availableCopies = copyRepository.findDistinctCopiesByLibrarySlugAndBookIdAndStatus(slug, bookId, CopyStatus.AVAILABLE);

        if(availableCopies.isEmpty()) {
            throw new CopyIsNotAvailableException();
        }
        else {
            Copy copy = availableCopies.get(0);
            List<User> users = userRepository.findByEmail(email);

            if (users == null || users.isEmpty()) {
                throw new UserNotFoundException();
            }

            User user = users.get(0);

            copy.setStatus(CopyStatus.BORROWED);
            copyRepository.save(copy);

            Loan loan = new LoanBuilder()
                    .withCopy(copy)
                    .withUser(user)
                    .build();

            return loanRepository.save(loan);
        }

    }


    public Loan returnCopy(Integer loanId) throws LoanNotExistsException {
        Loan loan = loanRepository.findOne(loanId);

        if(loan == null) {
            throw new LoanNotExistsException();
        }
        Copy copy = loan.getCopy();
        if (copy == null) {
            throw new LoanNotExistsException();
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
