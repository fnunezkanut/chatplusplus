package ie.nuigalway.sd3.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerTest {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;


    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
    }


    @Test
    public void should_say_test() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("test")
                ));
    }
}