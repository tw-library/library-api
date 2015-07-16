package com.thoughtworks.library.loan;

import com.thoughtworks.library.book.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    List<Loan> findByBook(Book borrowedBook);
}