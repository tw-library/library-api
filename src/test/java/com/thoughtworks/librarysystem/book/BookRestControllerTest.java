package com.thoughtworks.librarysystem.book;


import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class BookRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();


        book1 = new BookBuilder()
                .withTitle("The Girl Who Kicked the Hornet's Nest")
                .withAuthor("Stieg Larsson")
                .withSubtitle(null)
                .withDescription("\\u003cp\\u003e\\u003cb\\u003e\\u003cb\\u003eComing September 2015: \\u003ci\\u003eThe " +
                                "Girl in the Spider's Web\\u003c/i\\u003e\\u003c/b\\u003e\\u003c/b\\u003e\\u003cbr\\u003e\\u003cbr\\" +
                                "u003eIn the third volume of the Millennium series, Lisbeth Salander lies in critical condition in a " +
                                "Swedish hospital, a bullet in her head.\\u003cbr\\u003e\\u003cbr\\u003eBut she's fighting for her " +
                                "life in more ways than one: if and when she recovers, she'll stand trial for three murders. " +
                                "With the help of Mikael Blomkvist, she'll need to identify those in authority who have allowed " +
                                "the vulnerable, like herself, to suffer abuse and violence. And, on her own, she'll seek " +
                                "revenge--against the man who tried to killer her and against the corrupt government institutions " +
                                "that nearly destroyed her life.\\u003c/p\\u003e\\u003cbr\\u003e\\u003cbr\\u003e\\u003cbr\\u003e\\" +
                                "u003ci\\u003eFrom the Trade Paperback edition.\\u003c/i\\u003e")
                .withIsbn13(9780307593672l)
                .withPublisher("Knopf Doubleday Publishing Group")
                .withPublicationDate("2010-05-25")
                .withNumberOfPages(576)
                .withImageUrl("http://books.google.com.br/books/content?id=AZ5J6B1-4BoC&printsec=frontcover&img=1&zoom=0&edge=curl&i" +
                            "mgtk=AFLRE70ReZTXNuUwmOr-RLYgzWtcGOlub7PTKwaTz-cvQbtn8TD4HNVIis1iGKknPMWNzqoC0zfLRAu3t5b8wxgfP5FLJ51auTdxj7OZ" +
                            "tDb0QtN9iVgsSCrRr-5IjEBy66whBMJ8fY31&source=gbs_api")
                .build();

        bookRepository.save(book1);

        book2 = new BookBuilder()
                .withTitle("Flowers")
                .withAuthor("Vijaya Khisty Bodach")
                .withSubtitle(null)
                .withDescription("Kick off a plant unit right with Plant Parts from Pebble Plus. Clear text and large, " +
                                "striking photographs reveal each part of a plant and the role it plays in keeping plants" +
                                " thriving. Clear diagrams enhance the experience of exploring each part. So plant the " +
                                "seeds for a love of life science with Plant Parts!\"")
                .withIsbn13(9780736896191L)
                .withPublisher("Capstone")
                .withPublicationDate("2006-07-01")
                .withNumberOfPages(24)
                .withImageUrl("http://books.google.com.br/books/content?id=_ojXNuzgHRcC&printsec=frontcover&img=1&zoom=" +
                            "0&edge=curl&imgtk=AFLRE70fspnCQhDGto11Jg5K01WUluRvPUPA_CjnwthDsp4n5sY5cJ_Lp9AvvmYc80dYqMHAYGg1Mc" +
                            "wJb5XQKIF4lEWYMoDUVqu-Bu9Z9TQH-gbSAbUI99gKnqcZ9EV_K0K7Tefvzii7&source=gbs_api")
                .build();

        bookRepository.save(book2);

    }

    @Test
    public void shouldListAllBooks() throws Exception {

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$_embedded.books", hasSize(2)))
                .andExpect(jsonPath("$_embedded.books[0].title", is(book1.getTitle())))
                .andExpect(jsonPath("$_embedded.books[0].author", is(book1.getAuthor())))
                .andExpect(jsonPath("$_embedded.books[0].subtitle", is(book1.getSubtitle())))
                .andExpect(jsonPath("$_embedded.books[0].description", is(book1.getDescription())))
                .andExpect(jsonPath("$_embedded.books[0].isbn", is(book1.getIsbn())))
                .andExpect(jsonPath("$_embedded.books[0].publisher", is(book1.getPublisher())))
                .andExpect(jsonPath("$_embedded.books[0].publicationDate", is(book1.getPublicationDate())))
                .andExpect(jsonPath("$_embedded.books[0].numberOfPages", is(book1.getNumberOfPages())))
                .andExpect(jsonPath("$_embedded.books[0].imageUrl", is(book1.getImageUrl())))

                .andExpect(jsonPath("$_embedded.books[1].title", is(book2.getTitle())))
                .andExpect(jsonPath("$_embedded.books[1].author", is(book2.getAuthor())))
                .andExpect(jsonPath("$_embedded.books[1].subtitle", is(book2.getSubtitle())))
                .andExpect(jsonPath("$_embedded.books[1].description", is(book2.getDescription())))
                .andExpect(jsonPath("$_embedded.books[1].isbn", is(book2.getIsbn())))
                .andExpect(jsonPath("$_embedded.books[1].publisher", is(book2.getPublisher())))
                .andExpect(jsonPath("$_embedded.books[1].publicationDate", is(book2.getPublicationDate())))
                .andExpect(jsonPath("$_embedded.books[1].numberOfPages", is(book2.getNumberOfPages())))
                .andExpect(jsonPath("$_embedded.books[1].imageUrl", is(book2.getImageUrl())));

    }

}