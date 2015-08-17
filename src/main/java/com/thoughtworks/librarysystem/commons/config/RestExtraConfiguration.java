package com.thoughtworks.librarysystem.commons.config;

import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.loan.Loan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;


@Configuration
public class RestExtraConfiguration extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Loan.class);
    }


}