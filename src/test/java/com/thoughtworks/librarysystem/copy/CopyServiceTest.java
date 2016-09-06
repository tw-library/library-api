package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.commons.factories.CopyFactory;
import com.thoughtworks.librarysystem.commons.factories.UserFactory;
import com.thoughtworks.librarysystem.user.User;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rpinheir on 9/2/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CopyServiceTest {

    @Mock
    CopyRepository copyRepository;
    @InjectMocks
    CopyService service;

    private static  final String USER_EMAIL = "some@email.com";
    private static  final String SLUG_REC = "rec";

    private User user;
    private Copy copy;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setEmail(USER_EMAIL);

        copy = new CopyFactory().createStandardCopyWithSameIsbnAndLibrary();
        copy.setId(1);
        copy.setStatus(CopyStatus.AVAILABLE);

        List<Copy> copyList = Arrays.asList(copy);


        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug()))
                .thenReturn(copyList);
    }

    @Test
    public void shouldReturnCopiesInTheLibrary() throws Exception {
        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);
        List<Copy> expectedCopies = Arrays.asList(copy);
        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));
    }

    @Test
    public void shouldReturnCopiesInTheLibraryWithAvailableStatus() throws Exception {
        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);
        List<Copy> expectedCopies = Arrays.asList(copy);
        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));
    }

    @Test
    public void shouldReturnCopiesInTheLibraryWithBorrowedStatus() throws Exception {
        List<Copy> expectedCopies = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());
        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug()))
                .thenReturn(expectedCopies);

        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));
    }

    @Test
    public void shouldReturnCopiesInTheLibraryWithBorrowedAndAvailableStatus() throws Exception {
        List<Copy> expectedCopies = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(expectedCopies);

        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

    }

    @Test
    public void shouldReturnCopiesInTheLibraryWithAndAvailableStatusWithUserWithoutCopies() throws Exception {
        List<Copy> expectedCopies = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(expectedCopies);

        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));
    }

    @Test
    public void shouldReturnCopiesInTheLibraryWithAndAvailableStatusWithUserWithCopies() throws Exception {
        List<Copy> copiesFromLibrary = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());
        List<Copy> copiesFromUser = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        Set<Copy> expectedCopies = new HashSet<>(copiesFromLibrary);

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(copiesFromLibrary);
        when(copyRepository.findCopiesByLoansAndLoansUserId(user.getId())).thenReturn(copiesFromUser);


        Collection<Copy> actualCopies =  service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

        verify(copyRepository, times(1)).findCopiesByLibrarySlug(copy.getLibrary().getSlug());
        verify(copyRepository, times(1)).findCopiesByLoansAndLoansUserId(user.getId());
    }

    @Test
    public void shouldReturnCopiesWithTwoCopiesAvailableOfABook() throws Exception {
        List<Copy> copiesFromLibrary = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary(),
                new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());
        List<Copy> copiesFromUser = Collections.emptyList();

        Set<Copy> expectedCopies = new HashSet<>();
        expectedCopies.add(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(copiesFromLibrary);
        when(copyRepository.findCopiesByLoansAndLoansUserId(user.getId())).thenReturn(copiesFromUser);

        Collection<Copy> actualCopies = service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

        verify(copyRepository, times(1)).findCopiesByLibrarySlug(copy.getLibrary().getSlug());
        verify(copyRepository, times(1)).findCopiesByLoansAndLoansUserId(user.getId());
    }

    @Test
    public void shouldReturnCopiesWithTwoCopiesAvailableOfABookAndTheUserWithOneCopy() throws Exception {
        List<Copy> copiesFromLibrary = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary(),
                new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());
        List<Copy> copiesFromUser = Arrays.asList(new CopyFactory()
                .createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        Set<Copy> expectedCopies = new HashSet<>();
        expectedCopies.add(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(copiesFromLibrary);
        when(copyRepository.findCopiesByLoansAndLoansUserId(user.getId())).thenReturn(copiesFromUser);

        Collection<Copy> actualCopies = service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

        verify(copyRepository, times(1)).findCopiesByLibrarySlug(copy.getLibrary().getSlug());
        verify(copyRepository, times(1)).findCopiesByLoansAndLoansUserId(user.getId());
    }

    @Test
    public void shouldReturnCopiesWithTwoCopiesAvailableOfABookAndOtherUserWithOneCopy() throws Exception {
        List<Copy> copiesFromLibrary = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary(),
                new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        List<Copy> copiesFromUser = Collections.emptyList();

        Set<Copy> expectedCopies = new HashSet<>();
        expectedCopies.add(new CopyFactory().createStandardCopyWithSameIsbnAndLibrary());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(copiesFromLibrary);
        when(copyRepository.findCopiesByLoansAndLoansUserId(user.getId())).thenReturn(copiesFromUser);

        Collection<Copy> actualCopies = service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

        verify(copyRepository, times(1)).findCopiesByLibrarySlug(copy.getLibrary().getSlug());
        verify(copyRepository, times(1)).findCopiesByLoansAndLoansUserId(user.getId());
    }

    @Test
    public void shouldReturnCopiesWithTwoCopiesAvailableOfABookAndAllCopiesBorrowed() throws Exception {
        List<Copy> copiesFromLibrary = Arrays.asList(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed(),
                new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        List<Copy> copiesFromUser = Collections.emptyList();

        Set<Copy> expectedCopies = new HashSet<>();
        expectedCopies.add(new CopyFactory().createStandardCopyWithSameIsbnAndLibraryAndBorrowed());

        when(copyRepository.findCopiesByLibrarySlug(copy.getLibrary().getSlug())).thenReturn(copiesFromLibrary);
        when(copyRepository.findCopiesByLoansAndLoansUserId(user.getId())).thenReturn(copiesFromUser);

        Collection<Copy> actualCopies = service.findCopiesBySlugAndUser(copy.getLibrary().getSlug(), user);

        assertTrue(CollectionUtils.isEqualCollection(actualCopies, expectedCopies));

        verify(copyRepository, times(1)).findCopiesByLibrarySlug(copy.getLibrary().getSlug());
        verify(copyRepository, times(1)).findCopiesByLoansAndLoansUserId(user.getId());
    }
}
