package com.thoughtworks.library.book;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="status")
    private BookStatus status;

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() == this.getClass())) {
            return false;
        }

        Book other = (Book)obj;
        return Objects.equals(this.id, other.id) &&
                Objects.equals(this.title, other.title) &&
                Objects.equals(this.author, other.author) &&
                Objects.equals(this.status, other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.author, this.status);
    }
}