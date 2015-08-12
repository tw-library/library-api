package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotBorrowedException;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.commons.ResponseError;
import com.thoughtworks.librarysystem.loan.exceptions.LoanNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RepositoryRestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity borrowBook(@RequestBody @Valid Loan loan, BindingResult bindingResults) {

        try {

            if (bindingResults.hasErrors()) {

                List<ResponseError> responseErrors = new ArrayList<>();
                for (FieldError fieldError: bindingResults.getFieldErrors()) {
                    responseErrors.add(new ResponseError(fieldError.getField(), fieldError.getDefaultMessage()));
                }
                return new ResponseEntity(responseErrors,HttpStatus.PRECONDITION_FAILED);
            }

            loanService.borrowCopy(loan.getCopy(), loan.getEmail());

        }catch (CopyIsNotAvailableException e){
            return new ResponseEntity(new ResponseError(e.getMessage()),HttpStatus.PRECONDITION_FAILED);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity returnBook(@RequestBody Loan loan) {

        try {

            loanService.returnCopy(loan);

        }catch (CopyIsNotBorrowedException | LoanNotExistsException e){
            return new ResponseEntity(new ResponseError(e.getMessage()),HttpStatus.PRECONDITION_FAILED);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
