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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogoutControllerTest {

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

        //fetch admin user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "admin@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        this.mockSession.setAttribute("currentUser", dbUser);
    }


    @Test
    public void should_logout() throws Exception {

        String requestSessionId = this.mockSession.getId();

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/logout").session( this.mockSession ) )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.containsString("You have signed out")
                )
        )
        .andExpect(view().name("logout"))
        .andReturn();

        assertNotNull( mvcResult );

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        MockHttpSession session = (MockHttpSession)mvcResult.getRequest().getSession();
        String responseSessionId = session.getId();

        //response session id should not be the same as request session id (since it should be invalidated on logout)
        assertEquals( true, !requestSessionId.equals( responseSessionId ));
    }
}