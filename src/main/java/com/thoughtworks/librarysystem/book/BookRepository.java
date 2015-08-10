package com.thoughtworks.librarysystem.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByIsbn(@Param("isbn") Long isbn);

}
