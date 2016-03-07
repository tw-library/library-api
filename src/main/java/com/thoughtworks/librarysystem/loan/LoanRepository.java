package com.thoughtworks.librarysystem.loan;

import com.thoughtworks.librarysystem.copy.Copy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    List<Loan> findByCopy(Copy copy);

    List<Loan> findByEndDateIsNullAndCopyLibrarySlugAndCopyBookId(@Param("slug") String slug, @Param("book") Integer book);

    Long countByEndDateIsNullAndCopyLibrarySlugAndCopyBookIdAndUserEmail(@Param("slug") String slug, @Param("book") Integer book, @Param("email") String email);

    List<Loan> findByEndDateIsNullAndCopyLibrarySlugAndCopyBookIdAndUserEmail(@Param("slug") String slug, @Param("book") Integer book, @Param("email") String email);



}