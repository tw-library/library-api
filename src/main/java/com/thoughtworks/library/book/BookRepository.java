package com.thoughtworks.library.book;

import org.jboss.logging.annotations.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;


@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Integer>{

    @Override
    @RestResource(exported = false)
    public Book save(Book s);

    @Override
    @RestResource(exported = false)
    public void delete(Book t);

}
