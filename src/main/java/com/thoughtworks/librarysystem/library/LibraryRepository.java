package com.thoughtworks.librarysystem.library;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LibraryRepository extends CrudRepository<Library, Integer> {
}
