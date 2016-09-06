package com.thoughtworks.librarysystem.copy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.loan.Loan;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Table(name="copy")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Copy.findCopiesByLoansAndLoansUserId",
                query = "Select distinct c from Copy c join c.loans l join l.user u where u.id = :id")
})
@EqualsAndHashCode(exclude = {"loans", "library", "book", "lastLoan", "status"})
@ToString(exclude = {"loans", "library", "lastLoan"})
public class Copy {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="copy_gen" )
    @SequenceGenerator(name= "copy_gen", sequenceName = "copy_gen", allocationSize = 1)
    private Integer id;

    @Column(columnDefinition = "int default 0")
    private CopyStatus status;

    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Book book;

    @JoinColumn(name = "library_id", nullable = false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Library library;

    @OneToMany(mappedBy = "copy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans;

    @Transient
    private Loan lastLoan;

    public Loan getLastLoan() {

        if (getLoans() != null && !getLoans().isEmpty()) {

            Collections.sort(getLoans(), new Comparator<Loan>() {

                @Override
                public int compare(Loan o1, Loan o2) {
                    return o2.getId().compareTo(o1.getId());
                }

            });

            lastLoan = getLoans().get(0);
        }

        return lastLoan;
    }

}
