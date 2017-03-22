package ie.nuigalway.sd3;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationErrorTest {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @InjectMocks
    private ApplicationError applicationErrorController;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.applicationErrorController).build();
    }


    @Test
    public void test_http_error() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/pagethatshouldnotexist")
                        .contentType(MediaType.TEXT_HTML)
        )
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}