package com.thoughtworks.librarysystem.user;

import com.thoughtworks.librarysystem.Application;
import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.commons.factories.UserFactory;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext
@Transactional
public class UserRestControllerTest extends ApplicationTestBase {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new UserFactory().createUserWithUserName("elayne");

        userRepository.save(user);

    }

    @Test
    public void shouldCreateNewUser() throws  Exception{

        HashMap<String, Object> inputs = new HashMap<String, Object>();
        inputs.put("name", "Tulio Cruz");
        inputs.put("email", "tcruz@thoughtworks.com");

        String userJson = loadFixture("user.json", inputs);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                        .andExpect(status().isCreated())
                        .andDo(print());

    }

    @Test
    public void shouldGetUserByEmail() throws  Exception{
        String email = user.getEmail();
        String uri = "/users/search/findByEmail?email" + email;
        mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


}