package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.services.ThreadService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ThreadRepositoryTest {

    @Autowired
    private ThreadService threadService;

    //our jdbc template
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS threads (\n" +
                "  id int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  title varchar(255) CHARACTER SET utf8 DEFAULT NULL,\n" +
                "  dt_created datetime DEFAULT NULL,\n" +
                "  dt_updated datetime DEFAULT NULL,\n" +
                "  customer_id int(10) unsigned DEFAULT 0,\n" +
                "  PRIMARY KEY (id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;"
        );

        jdbcTemplate.execute("INSERT INTO threads (\n" +
                " title,\n" +
                " dt_created,\n" +
                " dt_updated\n" +
                ") VALUES (\n" +
                " \"test\",\n" +
                " DATE(NOW()),\n" +
                " DATE(NOW())\n" +
                ");"
        );
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS threads");
    }

    //there should only be one entry in the test table (see @before)
    @Test
    public void getThreads() throws Exception {

        List<Thread> threads = this.threadService.getThreads();
        assertEquals(1, threads.size());
    }


}