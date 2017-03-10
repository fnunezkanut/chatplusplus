package ie.nuigalway.sd3;

import ie.nuigalway.sd3.controllers.TestController;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"default"})
public class ApplicationTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        //our hi controller test
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
    }

    @Test
    public void test() throws Exception {

        //we request a http://localhost:8080/test and check for a "test" message in response
        mockMvc.perform(MockMvcRequestBuilders.get("/test").accept(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.equalTo("test")
                ));
    }
}
