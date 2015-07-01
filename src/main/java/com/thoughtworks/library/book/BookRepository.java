package com.thoughtworks.library.book;

import org.jboss.logging.annotations.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface BookRepository extends CrudRepository<Book, Integer>{

}
