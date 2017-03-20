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
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MysqlThreadRepositoryTest {

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
                " dt_updated,\n" +
                " customer_id\n" +
                ") VALUES (\n" +
                " \"test\",\n" +
                " DATE(NOW()),\n" +
                " DATE(NOW()),\n" +
                " 1\n" +
                ");"
        );
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS threads");
    }

    //there should only be one entry in the test table (see @before)
    @Test
    public void check_getThreads() throws Exception {

        List<Thread> threads = this.threadService.getThreads();
        assertEquals(1, threads.size());
    }


    @Test
    public void check_getThreadsByCustomerId() throws Exception {

        long customerId = 1L;

        List<Thread> threads = this.threadService.getThreadsByCustomerId( customerId );
        assertEquals(1, threads.size());

        assertEquals(true, threads.get(0).getCustomerId().equals( customerId ));
    }




    @Test
    public void check_getThread() throws Exception {

        long threadId = 1L;

        Thread thread = this.threadService.getThread( threadId );
        assertEquals( true, thread instanceof Thread);
        assertEquals( true, thread.getId().equals( threadId ));

        //truncate everything in threads table so its empty
        jdbcTemplate.execute("TRUNCATE TABLE threads");

        //check single fetch
        try{

            this.threadService.getThread( 1L );
            fail("#1 exception is expected");
        }
        catch (Exception e){

            assert(true);
        }
    }


    @Test
    public void check_createThread() throws Exception{

        long customerId = 1L;
        String title = "test title";

        Long threadId = this.threadService.createThread( title, customerId );

        //should have an autoinsert id of 2
        assertEquals( true, threadId.equals(2L) );

        jdbcTemplate.execute("DROP TABLE IF EXISTS threads");

        //check exception
        try{

            this.threadService.createThread( title, customerId );
            fail("no threads table exists");
        }
        catch (Exception e){

            assert( true );
        }
    }


    @Test
    public void check_updateDtUpdated(){

        long threadId = 1L;

        try{

            threadService.updateDtUpdated( threadId );
        }
        catch (Exception e){

            fail( e.getMessage() );
        }


        jdbcTemplate.execute("DROP TABLE IF EXISTS threads");

        //check exception
        try{

            threadService.updateDtUpdated( threadId );
            fail("no threads table exists");
        }
        catch (Exception e){

            assert( true );
        }
    }


    @Test
    public void test_empty_table() throws Exception {


        jdbcTemplate.execute("DROP TABLE IF EXISTS threads");

        //check multifetch
        try{

            List<Thread> threads = this.threadService.getThreads( );
            if( threads.size() == 0 ){
                assert(true);
            }
            else{
                fail("#2 exception is expected");
            }
        }
        catch (Exception e){

            assert(true);
        }


        //check customer thread fetch
        try{

            List<Thread> threads = this.threadService.getThreadsByCustomerId( 1L );
            if( threads.size() == 0 ){
                assert(true);
            }
            else{
                fail("#3 exception is expected");
            }
        }
        catch (Exception e){

            assert(true);
        }
    }
}