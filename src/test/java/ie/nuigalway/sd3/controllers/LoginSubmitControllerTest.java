package ie.nuigalway.sd3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.nuigalway.sd3.ApplicationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.UUID;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginSubmitControllerTest {


    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected MockHttpSession mockSession;


    @Before
    public void setup(){

        this.mockMvc = webAppContextSetup( this.wac ).build();
        mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString() );
    }


    //tries to login as admin user (id=1)
    @Test
    public void should_login_as_admin()  throws Exception{

        String password = "password";
        String email = "admin@example.com";

        MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post( "/login/submit" )
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", email)
                .param("pass", password )
                .session(mockSession)
        )
        .andExpect(status().isOk() )
        .andReturn();

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );

        long retrievedUserId = Long.parseLong( ar.getFromPayload("user_id").toString() );


        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("signedin"));
        assertEquals( 1L, retrievedUserId ); //user id should be 1
    }


    //tries to login as a user that's no in database
    @Test
    public void should_not_login_unknown_user() throws Exception{

        String password = "test";
        String email = "test@test.com";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post( "/login/submit" )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("email", email)
                        .param("pass", password )
                        .session(mockSession)
        )
                .andExpect(status().isNotFound() )
                .andReturn();

        //get result
        MockHttpServletResponse response = mvcResult.getResponse();

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response.getContentAsString(), ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("no such user"));
    }
}