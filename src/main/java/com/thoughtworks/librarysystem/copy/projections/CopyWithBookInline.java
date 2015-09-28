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

    @Value("#{target.book.subtitle}")
    String getSubtitle();

    @Value("#{target.book.description}")
    String getDescription();

    @Value("#{target.book.isbn}")
    Long getIsbn();

    @Value("#{target.book.publisher}")
    String getPublisher();

    @Value("#{target.book.publicationDate}")
    String getPublicationDate();

    @Value("#{target.book.numberOfPages}")
    Integer getNumberOfPages();

    Loan getLastLoan();

    List<Loan> getLoans();

}
