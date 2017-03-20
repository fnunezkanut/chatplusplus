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


    @Test
    public void test_getThreads_as_unauthorized_user() throws Exception {


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get( "/api/v1/threads" )
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError() )
                .andReturn();

    }


    @Test
    public void test_getThreads_as_customer() throws Exception {

        //fetch customer user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "customer@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get( "/api/v1/threads" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockSession)
        )
                .andExpect(status().is4xxClientError() )
                .andReturn();


        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );


        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("current user is not a support person"));
    }


    @Test
    public void test_getThreadsMy() throws Exception {


        //fetch customer user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "customer@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);



        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get( "/api/v1/threads/my" )
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



    @Test
    public void test_getThreadsMy_as_unauthorized_user() throws Exception {


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get( "/api/v1/threads/my" )
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError() )
                .andReturn();


        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );


        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("current user is not signed in"));
    }



    @Test
    public void test_getThreadsMy_as_user_with_no_threads() throws Exception {

        //fetch admin user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "admin@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        //admin user shouldnt have any entries
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get( "/api/v1/threads/my" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockSession)
        )
                .andExpect(status().isOk())
                .andReturn();

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );


        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( 0, ar.getPayload().size());
    }


    @Test
    public void test_createThread()  throws Exception{

        //fetch customer user and create a mock session for him
        User dbUser = new User();
        String pass = "password";
        String email = "customer@example.com";
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();
        dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);
        mockSession.setAttribute("currentUser", dbUser);


        String title = "test thread";



        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post( "/api/v1/threads" )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("title", title )
                        .session(mockSession)
        )
                .andExpect(status().isOk() )
                .andReturn();

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("created"));
    }



    @Test
    public void test_createThread_as_unauthorized_user() throws Exception {

        String title = "test thread";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post( "/api/v1/threads" )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("title", title )
        )
                .andExpect(status().is4xxClientError() )
                .andReturn();


        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("current user is not signed in"));
    }

}