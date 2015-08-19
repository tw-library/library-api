package com.thoughtworks.librarysystem.copy.projections;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.Loan;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "copyWithBookInline", types = { Copy.class })
public interface CopyWithBookInline {

    Integer getId();

    CopyStatus getStatus();

    String getDonator();

    Book getBook();

    Loan getLastLoan();

}
