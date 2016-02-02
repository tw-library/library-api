package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.loan.exceptions.LoanNotExistsException;
import com.thoughtworks.librarysystem.loan.exceptions.UserNotFoundException;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    @InjectMocks
    LoanController controller = new LoanController();

    @Mock
    LoanService loanService;

    private User user;
    private Book book;
    private Copy copy;
    private Loan loan;
    private BindingResult bindingResult;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("some@email.com");

        book = new Book();

        copy = new CopyBuilder()
                .withBook(book)
                .build();

        loan = new LoanBuilder()
                .withUser(user)
                .withCopy(copy)
                .withId(1)
                .build();

        bindingResult = mock(BindingResult.class);
    }

    @Test
    public void shouldReturnHttpStatusCreatedWhenBookIsSuccessfullyBorrowed() {
        when(loanService.borrowCopy(copy.getId(), user.getEmail())).thenReturn(loan);

        ResponseEntity currentResponse = controller.borrowBook(loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.CREATED);

        assertThat(currentResponse, is(expectedResponse));
    }

    @Test
    public void shouldReturnHttpConflictStatusWhenBookIsNotAvailable() {
        doThrow(new CopyIsNotAvailableException()).when(loanService).borrowCopy(copy.getId(), user.getEmail());

        ResponseEntity currentResponse = controller.borrowBook(loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.CONFLICT);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnHttpInternalServerErrorStatusForGenericExceptionWhenBorrowingBook(){
        Loan invalidLoan = null;

        ResponseEntity currentResponse = controller.borrowBook(invalidLoan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnHttpUnauthorizedStatusCodeWhenUserIsNotFound() {
        User notExitentUser = new UserBuilder()
                .withEmail("user@notfound.com")
                .build();

        loan.setUser(notExitentUser);

        doThrow(new UserNotFoundException()).when(loanService).borrowCopy(loan.getCopy().getId(), loan.getEmail());

        ResponseEntity currentResponse = controller.borrowBook(loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.UNAUTHORIZED);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnHttpPreconditionFailedStatusCodeThereAreBindingErrors(){
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity currentResponse = controller.borrowBook(loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.PRECONDITION_FAILED);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnHttpNoContentStatusWhenBookIsSuccessfullyReturned(){
        when(loanService.returnCopy(loan.getId())).thenReturn(loan);

        ResponseEntity currentResponse = controller.returnBook(loan.getId(), loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NO_CONTENT);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnPreconditionRequiredHttpStatusWhenLoanDoesNotExist(){
        Loan notExistentLoan = new Loan();

        doThrow(LoanNotExistsException.class).when(loanService).returnCopy(notExistentLoan.getId());

        ResponseEntity currentResponse = controller.returnBook(notExistentLoan.getId(), notExistentLoan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.PRECONDITION_REQUIRED);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }

    @Test
    public void shouldReturnInTernalServerStatusCodeForAnyGenericExceptionWhenReturningBook(){
        doThrow(Exception.class).when(loanService).returnCopy(loan.getId());

        ResponseEntity currentResponse = controller.returnBook(loan.getId(), loan, bindingResult);
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(currentResponse.getStatusCode(), is(expectedResponse.getStatusCode()));
    }
}