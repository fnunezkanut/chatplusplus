package ie.nuigalway.sd3.controllers.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import ie.nuigalway.sd3.ApplicationException;
import ie.nuigalway.sd3.ApplicationResponse;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
import ie.nuigalway.sd3.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadControllerTest {

    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected MockHttpSession mockSession;



    @Before
    public void setup() throws Exception{

        this.mockMvc = webAppContextSetup( this.wac ).build();
        mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString() );

        //fetch admin user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "admin@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);
    }


    @Test
    public void testMockCreation(){
        assertNotNull(threadService);
        assertNotNull(userService);
    }



    @Test
    public void test_getThreads() throws Exception {

        //we want to pass a signed in user session cookie into our request
        User currentUser = (User)mockSession.getAttribute("currentUser");

        MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.get( "/api/v1/threads" )
                    .contentType(MediaType.APPLICATION_JSON)
                    .session(mockSession)
            )
            .andExpect(status().isOk() )
            .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON_UTF8_VALUE ))
            .andReturn();

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("fetched"));

        //examine payload
        HashMap<String, Object> threads = ar.getPayload();

        assertEquals( true, threads.size() > 0 );
    }
}