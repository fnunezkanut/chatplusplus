package ie.nuigalway.sd3.controllers;

import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupportControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected MockHttpSession mockSession;


    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
        mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }


    @Test
    public void check_support_as_unauthorized_user() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/support")
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }


    @Test
    public void check_support_as_support_user() throws Exception {


        //fetch admin user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "admin@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/support")
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.containsString("Customer Issues [Support View]")
                ))
                .andExpect(view().name("support/support"))
                .andReturn();

    }


    @Test
    public void check_support_as_customer() throws Exception {

        //fetch admin user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "customer@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/support")
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.containsString("Customer Support Center")
                ))
                .andExpect(view().name("support/customer"))
                .andReturn();

    }
}