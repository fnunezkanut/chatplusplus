package ie.nuigalway.sd3.controllers;

import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {

    @Autowired
    private ThreadService threadService;

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
    public void check_chat_without_threadId() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/chat")
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void check_chat_as_unauthorized_user() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/chat?threadId=0")
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }


    @Test
    public void check_chat_as_customer() throws Exception {

        //extract first thread
        Thread thread = threadService.getThreads().get(0);


        //fetch customer user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "customer@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/chat?threadId=" + thread.getId() )
                        .contentType(MediaType.TEXT_HTML)
                        .session(mockSession)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.containsString(thread.getTitle() )
                ))
                .andExpect(view().name("chat"))
                .andReturn();



    }


}