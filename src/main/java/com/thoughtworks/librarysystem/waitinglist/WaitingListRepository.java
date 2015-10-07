package com.thoughtworks.librarysystem.waitinglist;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by dalcocer on 9/29/15.
 */

@RepositoryRestResource
public interface WaitingListRepository extends CrudRepository<WaitingList,Integer> {

    List<WaitingList> findByBookAndLibrary(@Param ("book_id") Book book_id, @Param("slug") Library library_id);
}
