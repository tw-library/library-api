package com.thoughtworks.librarysystem.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByEmail(@Param("email") String email);

}
