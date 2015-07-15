package com.thoughtworks.library.loan;

import com.thoughtworks.library.book.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface LoanRepository extends CrudRepository<Loan, Integer> , LoanRepositoryCustom{

}