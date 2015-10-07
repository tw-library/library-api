package com.thoughtworks.librarysystem.waitinglist;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * Created by dalcocer on 9/29/15.
 */

@Table(name="waitinglist")
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Data
public class WaitingList {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="waitinglist_gen" )
    @SequenceGenerator(name= "waitinglist_gen", sequenceName = "waitinglist_gen", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="book_id")
    private Book book;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="library_id")
    private Library library;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "start_date")
    @NotNull
    private String startDate;

    @Column(name = "end_date")
    @NotNull
    private String endDate;

    @PrePersist
    void onCreate() {
        this.startDate = new Date(System.currentTimeMillis()).toString();
    }

}
