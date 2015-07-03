package com.thoughtworks.library.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Integer>{

  /**  @Override
    @RestResource(exported = false)
    Book save(Book s);

    @Override
    @RestResource(exported = false)
    void delete(Book t);**/

}
