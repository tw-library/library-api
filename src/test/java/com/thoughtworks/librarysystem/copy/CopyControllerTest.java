package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.loan.exceptions.EmailNotFoundException;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rpinheir on 9/1/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CopyControllerTest {

    @Mock
    CopyService copyService;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    CopyController copyController;

    @Test
    public void shouldReturnAvailableCopies() throws Exception {
        User user = new UserBuilder().withEmail("utester@thoughtworks.com").build();
        when(userRepository.findByEmail("utester@thoughtworks.com")).thenReturn(
                Arrays.asList(user)
        );
        when(copyService.findCopiesBySlugAndUser("rec", user)).thenReturn(Collections.emptyList());


        Collection<Copy> actualAvailableCopies = copyController.listCopies("rec", "utester@thoughtworks.com");
        List<Copy> expectedAvailableCopies = Arrays.asList();

        Assert.assertArrayEquals(actualAvailableCopies.toArray(), expectedAvailableCopies.toArray());

    }

    @Test(expected = EmailNotFoundException.class)
    public void shouldReturnFailWhenUserNotFound() {
        User user = new UserBuilder().withEmail("utester@thoughtworks.com").build();
        when(userRepository.findByEmail("utester@thoughtworks.com")).thenReturn(
                Collections.emptyList()
        );

        copyController.listCopies("rec", "utester@thoughtworks.com");
    }
}
