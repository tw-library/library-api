package loan;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.book.Book;
import com.thoughtworks.librarysystem.book.BookBuilder;
import com.thoughtworks.librarysystem.book.BookRepository;
import com.thoughtworks.librarysystem.copy.Copy;
import com.thoughtworks.librarysystem.copy.CopyBuilder;
import com.thoughtworks.librarysystem.copy.CopyRepository;
import com.thoughtworks.librarysystem.copy.CopyStatus;
import com.thoughtworks.librarysystem.library.Library;
import com.thoughtworks.librarysystem.library.LibraryBuilder;
import com.thoughtworks.librarysystem.library.LibraryRepository;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;
import com.thoughtworks.librarysystem.user.UserRepository;
import commons.ApplicationTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class BorrowBookRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    CopyRepository copyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LibraryRepository libraryRepository;

    private Copy copy;
    private User user;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BookRepository bookRepository;

    private Book book;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        book = new BookBuilder()
                .withAuthor("BOOK 1 AUTHOR EXAMPLE")
                .withTitle("BOOK 1 NAME EXAMPLE")
                .build();

        bookRepository.save(book);

    }

    @Test
    public void shouldLoanBookWhenItIsAvailable() throws  Exception{

        Library library = new LibraryBuilder()
                .withName("Teste")
                .withSlug("T")
                .build();

        libraryRepository.save(library);


        copy = new CopyBuilder()
                        .withBook(book)
                        .withLibrary(library)
                        .build();

        copyRepository.save(copy);

        user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userRepository.save(user);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", user.getEmail());

        String loanJson = loadFixture("borrow_a_book.json", inputs);
        
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanJson))
                        .andExpect(status().isCreated());

        Copy borrowedBookCopy = copyRepository.findOne(copy.getId());

        Assert.assertThat(borrowedBookCopy.getStatus(), is(CopyStatus.BORROWED));

    }

    @Test
    public void shouldNotLoanBookWhenItIsAlreadyBorrowed() throws  Exception{

        copy = new CopyBuilder()
                .withBook(book)
                .withStatus(CopyStatus.BORROWED)
                .build();

        user = new UserBuilder()
                .withEmail("tcruz@thoughtworks.com")
                .withName("Tulio")
                .build();

        userRepository.save(user);

        copyRepository.save(copy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("email", user.getEmail());

        String loanJson = loadFixture("borrow_a_book.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isConflict());

    }

    @Test
    public void shouldNotLoanBookWhenItIsAvailableButYouDoNotIdentifyYourself() throws  Exception{

        copy = new CopyBuilder()
                .withBook(book)
                .build();

        user = new UserBuilder()
                .withEmail("")
                .withName("Tulio")
                .build();

        userRepository.save(user);

        copyRepository.save(copy);

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("copy", copy);
        inputs.put("user", user);


        String loanJson = loadFixture("borrow_a_book.json", inputs);

        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanJson))
                .andExpect(status().isPreconditionFailed());
    }
}