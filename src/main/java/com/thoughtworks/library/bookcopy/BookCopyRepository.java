package com.thoughtworks.library.bookcopy;

import com.thoughtworks.library.book.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookCopyRepository extends CrudRepository<BookCopy, Integer> {
}
