package auth;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.commons.config.AuthenticationFilter;
import commons.ApplicationTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class AuthTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    private AuthenticationFilter securityFilter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {

        DefaultMockMvcBuilder defaultMockMvcBuilder = webAppContextSetup(webApplicationContext);
        defaultMockMvcBuilder.addFilters(securityFilter);
        this.mockMvc = defaultMockMvcBuilder.build();

    }

    @Test
    public void shouldReturnForbiddenWhenTokenNotExists() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isForbidden());
    }

}


