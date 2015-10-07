package com.thoughtworks.librarysystem.waitinglist;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.user.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

import java.sql.Date;

/**
 * Created by dalcocer on 9/29/15.
 */

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class WaitingListBuilder {

    private Book book;
    private Library library;
    private User user;
    private String startDate;
    private String endDate;


    public WaitingList build(){

        WaitingList list = new WaitingList();
        list.setBook(book);
        list.setLibrary(library);
        list.setUser(user);
        list.setStartDate(startDate);
        list.setEndDate(endDate);

        return list;
    }
}
