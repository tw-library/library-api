package com.thoughtworks.librarysystem.copy;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by rpinheir on 9/2/16.
 */
@Component
public class CopyService {

    @Autowired
    private CopyRepository copyRepository;

    public Collection<Copy> findCopiesBySlugAndUser(String slug, User user) {

        List<Copy> copiesFromLibrary = copyRepository.findCopiesByLibrarySlug(slug);
        List<Copy> copiesFromUser = copyRepository.findCopiesByLoansAndLoansUserId(user.getId());
        Map<Book, Copy> books = new HashMap<>();

        for (Copy copy : copiesFromLibrary) {
            if (copiesFromUser.contains(copy)) {
                copy.setStatus(CopyStatus.BORROWED);
            }

            books.put(copy.getBook(), copy);

        }

//        Stream.of(copies).filter(s -> {
//            return copiesFromUser.contains(s);
//        })
//                .forEach(s -> ((Copy) s).setStatus(CopyStatus.BORROWED));
        return  books.values();

    }
}
