package com.thoughtworks.library.loan;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;

public class LoanRepositoryImpl implements LoanRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public Loan save(Loan entity) {

        if (entity.getId() == null) {

            entity.setStartDate(new Date(System.currentTimeMillis()));
        }

        em.persist(entity);

        return entity;
    }
}
