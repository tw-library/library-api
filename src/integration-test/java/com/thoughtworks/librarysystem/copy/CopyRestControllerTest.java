package com.thoughtworks.librarysystem.copy;


import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.commons.factories.BookFactory;
import com.thoughtworks.librarysystem.commons.factories.CopyFactory;
import com.thoughtworks.librarysystem.commons.factories.LibraryFactory;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.loan.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class CopyRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    CopyRepository copyRepository;

    @Autowired
    private LoanService loanService;

    private Book book, bookUnic;
    private Copy copy, copySecond, copyThird, copyFourthPOA;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        book = new BookFactory().createBookWithStandardIsbn();
        bookUnic = new BookFactory().createBookWithoutIsbn();

        bookRepository.save(book);
        bookRepository.save(bookUnic);

        Library libraryBH = new LibraryFactory().createStardardLibrary();
        Library libraryPOA = new LibraryFactory().createLibrary("Porto Alegre","POA");

        libraryRepository.save(libraryBH);
        libraryRepository.save(libraryPOA);

        copy = new CopyFactory().createCopyWithLibraryAndBook(libraryBH, book);
        copySecond = new CopyFactory().createCopyWithLibraryAndBook(libraryBH, book);
        copySecond.setStatus(CopyStatus.BORROWED);
        copyThird = new CopyFactory().createCopyWithLibraryAndBook(libraryBH, bookUnic);

        copyFourthPOA = new CopyFactory().createCopyWithLibraryAndBook(libraryPOA, book);
        copyRepository.save(copyFourthPOA);
        copyRepository.save(copy);
        copyRepository.save(copySecond);
        copyRepository.save(copyThird);

    }

    @Test
    public void shouldListAllFourCopiesOfLibraries() throws Exception {
        mockMvc.perform(get("/copies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(print());
    }

    @Test
    public void shouldVerifyCopiesAtributes() throws Exception {
        mockMvc.perform(get("/copies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$_embedded.copies[0].id", is(copy.getId())))
                .andExpect(jsonPath("$_embedded.copies[0].status", is(copy.getStatus().name())))
                .andExpect(jsonPath("$_embedded.copies[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$_embedded.copies[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$_embedded.copies[0].imageUrl", is(book.getImageUrl())))
                .andExpect(jsonPath("$_embedded.copies[0].reference", is(book.getId())))
                .andExpect(jsonPath("$_embedded.copies[0].subtitle", is(book.getSubtitle())))
                .andExpect(jsonPath("$_embedded.copies[0].description", is(book.getDescription())))
                .andExpect(jsonPath("$_embedded.copies[0].isbn", is(book.getIsbn())))
                .andExpect(jsonPath("$_embedded.copies[0].publisher", is(book.getPublisher())))
                .andExpect(jsonPath("$_embedded.copies[0].publicationDate", is(book.getPublicationDate())))
                .andExpect(jsonPath("$_embedded.copies[0].numberOfPages", is(book.getNumberOfPages())))
                .andDo(print());
    }



    @Test
    public void shouldListAllThreeCopiesByLibrarySlug() throws Exception {
        mockMvc.perform(get("/copies/search/findCopiesByLibrarySlug?slug=BH").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$_embedded.copies", hasSize(3)));
    }

    @Test
    public void shouldListTwoDistinctCopiesByLibraryBH() throws Exception {
        mockMvc.perform(get("/copies/search/findDistinctCopiesByLibrary?slug=BH").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$_embedded.copies", hasSize(2)))
                .andDo(print());

    }

    @Test
    public void shouldHaveOneAvailableBooksOfTwo() throws Exception {
        Integer book_id = copy.getBook().getId();
        String uri = "/copies/search/countByLibrarySlugAndBookIdAndStatus?slug=BH&book=" + book_id + "&status=AVAILABLE";
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());

    }


    @Test
    public void shouldHaveTwoCopiesOfABook() throws Exception {
        Integer book_id = copy.getBook().getId();
        String uri = "/copies/search/countByLibrarySlugAndBookId?slug=BH&book=" + book_id;
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("2"))
                .andDo(print());

    }


}