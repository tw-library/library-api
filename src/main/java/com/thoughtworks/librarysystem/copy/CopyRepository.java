package com.thoughtworks.librarysystem.copy;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CopyRepository extends CrudRepository<Copy, Integer> {
}
