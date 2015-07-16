package com.thoughtworks.library.loan;

import com.thoughtworks.library.exceptions.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RepositoryRestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveLoan(@RequestBody Loan loan) {

        try {
            loanService.borrowBook(loan.getBook());
        }catch (BookNotAvailableException e){
            return new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
