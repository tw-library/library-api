package copy;


import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.loan.LoanService;
import commons.ApplicationTestBase;
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

    private Book book;
    private Copy copy;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        book = new BookBuilder()
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

        bookRepository.save(book);

        Library library = new LibraryBuilder()
                .withName("belohorizonte")
                .withSlug("bh")
                .build();

        libraryRepository.save(library);

        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .withDonator("Tulio Cruz")
                .build();

        copyRepository.save(copy);
    }

    @Test
    public void shouldListAllCopiesWithBookInline() throws Exception {

            mockMvc.perform(get("/copies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$_embedded.copies", hasSize(1)))
                .andExpect(jsonPath("$_embedded.copies[0].id", is(copy.getId())))
                .andExpect(jsonPath("$_embedded.copies[0].status", is(copy.getStatus().name())))
                .andExpect(jsonPath("$_embedded.copies[0].donator", is(copy.getDonator())))
                .andExpect(jsonPath("$_embedded.copies[0].title", is(book.getTitle())))
                .andExpect(jsonPath("$_embedded.copies[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$_embedded.copies[0].imageUrl", is(book.getImageUrl())))
                .andExpect(jsonPath("$_embedded.copies[0].reference", is(book.getId())))
                .andExpect(jsonPath("$_embedded.copies[0].subtitle", is(book.getSubtitle())))
                .andExpect(jsonPath("$_embedded.copies[0].description", is(book.getDescription())))
                .andExpect(jsonPath("$_embedded.copies[0].isbn", is(book.getIsbn())))
                .andExpect(jsonPath("$_embedded.copies[0].publisher", is(book.getPublisher())))
                .andExpect(jsonPath("$_embedded.copies[0].publicationDate", is(book.getPublicationDate())))
                .andExpect(jsonPath("$_embedded.copies[0].numberOfPages", is(book.getNumberOfPages())));


    }

}