package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.user.User;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    @InjectMocks
    LoanController controller = new LoanController();

    @Mock
    LoanService loanService;

    @Test
    public void shouldReturnHttpStatusCreatedWhenBookIsBorrowed() throws Exception {
        User user = new User();
        user.setEmail("some@email.com");

        Book book = new Book();

        Copy copy = new CopyBuilder()
                .withBook(book)
                .build();

        Loan loan = new LoanBuilder()
                .withUser(user)
                .withCopy(copy)
                .withId(1)
                .build();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(loanService.borrowCopy(copy.getId(), user.getEmail())).thenReturn(loan);
        ResponseEntity currentResponse = controller.borrowBook(loan, bindingResult);

        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.CREATED);

        assertThat(currentResponse, is(expectedResponse));
    }
}