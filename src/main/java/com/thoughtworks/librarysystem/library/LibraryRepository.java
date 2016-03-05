package com.thoughtworks.librarysystem.library;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface LibraryRepository extends CrudRepository<Library, Integer> {

    List<Library> findBySlug(@Param("slug") String slug);

}




