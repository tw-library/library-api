package waitinglist;

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
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
import com.thoughtworks.librarysystem.waitinglist.WaitingList;
import com.thoughtworks.librarysystem.waitinglist.WaitingListBuilder;
import com.thoughtworks.librarysystem.waitinglist.WaitingListRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class WaitingListRestControllerTest{

    private MockMvc mockMvc;

    @Autowired
    WaitingListRepository waitingListRepository;

    private WaitingList list1;
    private WaitingList list2;

    @Autowired
    CopyRepository copyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LibraryRepository libraryRepository;

    private Copy copy;
    private User user;
    private User user2;
    private Library library;

    @Autowired
    BookRepository bookRepository;

    private Book book;


    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(context).build();

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();

        bookRepository.save(book);

         library = new LibraryBuilder()
                .withName("Test")
                .withSlug("T")
                .build();

        libraryRepository.save(library);


        copy = new CopyBuilder()
                .withBook(book)
                .withLibrary(library)
                .build();

        copyRepository.save(copy);

        user = new UserBuilder()
                .withEmail("dalcocer@thoughtworks.com")
                .withName("Diego")
                .build();
        user2 = new UserBuilder()
                .withEmail("dcorrales@thoughtworks.com")
                .withName("David")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        list1 = new WaitingListBuilder()
                .withBook(book)
                .withLibrary(library)
                .withUser(user)
                .withStartDate("2015-09-29")
                .withEndDate("")
                .build();
        waitingListRepository.save(list1);

        list2 = new WaitingListBuilder()
                .withBook(book)
                .withLibrary(library)
                .withUser(user2)
                .withStartDate("2015-09-29")
                .withEndDate("")
                .build();

                waitingListRepository.save(list2);
    }

    @Test
    public void shouldGetWaitingLists() throws Exception{

                mockMvc.perform(get("/waitingLists"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaTypes.HAL_JSON))
                        .andExpect(jsonPath("$._embedded.waitingLists", hasSize(2)));

    }

}
