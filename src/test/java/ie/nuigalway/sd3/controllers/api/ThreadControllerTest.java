package ie.nuigalway.sd3.controllers.api;

import ie.nuigalway.sd3.ApplicationException;
import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
import ie.nuigalway.sd3.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadControllerTest {

    @Mock
    private ThreadService threadService;

    @Mock
    private UserService userService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TestRestTemplate restTemplate;

    //testwide mockmvc object
    MockMvc mockMvc;


    @Before
    public void setup(){

        this.mockMvc = webAppContextSetup( this.wac ).build();
    }


    @Test
    public void testMockCreation(){
        assertNotNull(threadService);
        assertNotNull(userService);
    }



    @Test
    public void getThreads() throws Exception {

        MockHttpSession mockHttpSession = new MockHttpSession();
        String password = "password";
        String email = "admin@example.com";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post( "/login/submit" )
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("email", email)
                        .param("pass", password )
                )
                .andExpect(status().isOk() )
                .andReturn();




        List<Thread> threads = new ArrayList<Thread>();

        when(threadService.getThreads()).thenReturn((List<Thread>) threads);

        MvcResult mvcResult2 = mockMvc.perform( MockMvcRequestBuilders.get( "/api/v1/threads" ) ).andReturn();


//        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/threads").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk());


        //TODO
    }
}