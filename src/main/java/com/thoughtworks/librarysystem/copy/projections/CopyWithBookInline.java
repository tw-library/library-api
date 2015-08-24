package com.thoughtworks.librarysystem.copy.projections;

import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.loan.Loan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import javax.persistence.Column;
import java.util.List;

@Projection(name = "copyWithBookInline", types = { Copy.class })
public interface CopyWithBookInline {

    Integer getId();

    CopyStatus getStatus();

    String getDonator();

    @Value("#{target.book.title}")
    String getTitle();

    @Value("#{target.book.author}")
    String getAuthor();

    @Value("#{target.book.imageUrl}")
    String getImageUrl();

    @Value("#{target.book.id}")
    Integer getReference();

    Loan getLastLoan();

    List<Loan> getLoans();

}
