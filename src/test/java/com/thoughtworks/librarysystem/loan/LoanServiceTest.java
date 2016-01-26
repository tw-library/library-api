package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.exceptions.CopyIsNotAvailableException;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @Mock
    CopyRepository copyRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    @Resource
    LoanService service;

    @Mock
    LoanRepository loanRepository;

    private final String USER_EMAIL = "some@email.com";
    private final int COPY_ID = 1;

    private User user;
    private Copy copy;
    private Loan loan;

    @Before
    public void setUp() throws Exception {
        loan = new Loan();

        user = new User();
        user.setEmail(USER_EMAIL);

        copy = new Copy();
        copy.setId(COPY_ID);

        List<User> userList = Arrays.asList(user);

        when(copyRepository.findOne(COPY_ID)).thenReturn(copy);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(userRepository.findByEmail(anyString())).thenReturn(userList);

    }

    @Test
    public void shouldSetCopyStatusToBorrowed() throws Exception {
        copy.setStatus(CopyStatus.AVAILABLE);
        service.borrowCopy(copy.getId(), USER_EMAIL);

        assertThat(copy.getStatus(), is(CopyStatus.BORROWED));
    }

    @Test(expected = CopyIsNotAvailableException.class)
    public void shouldThrowExceptionWhenBorrowingAlreadyBorrowedCopy() throws Exception {
        copy.setStatus(CopyStatus.BORROWED);
        service.borrowCopy(copy.getId(), USER_EMAIL);
    }
}