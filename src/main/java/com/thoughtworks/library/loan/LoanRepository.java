package com.thoughtworks.library.loan;

import com.thoughtworks.library.book.Book;
import com.thoughtworks.library.bookcopy.BookCopy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LoanRepository extends CrudRepository<Loan, Integer> {

    List<Loan> findByBookCopy(BookCopy borrowedBookCopy);
}