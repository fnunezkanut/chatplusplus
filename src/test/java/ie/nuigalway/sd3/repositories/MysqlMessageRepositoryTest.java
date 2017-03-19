package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.Message;
import ie.nuigalway.sd3.entities.MessageView1;
import ie.nuigalway.sd3.services.MessageService;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MysqlMessageRepositoryTest {

    @Autowired
    private MessageService messageService;

    //our jdbc template
    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Before
    public void setUp() {

        jdbcTemplate.execute("CREATE TABLE messages (\n" +
                "  id int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  thread_id int(10) unsigned DEFAULT NULL,\n" +
                "  user_id int(10) unsigned DEFAULT '0',\n" +
                "  comment text CHARACTER SET utf8,\n" +
                "  dt_created datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  KEY s_user_id (user_id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n"
        );

        jdbcTemplate.execute("INSERT INTO messages (\n" +
                "\tthread_id,\n" +
                "\tuser_id,\n" +
                "\tcomment,\n" +
                "\tdt_created\n" +
                ") VALUES (\n" +
                "\t1,\n" +
                "\t1,\n" +
                "\t\"test\",\n" +
                "\tDATE(NOW())\n" +
                ");"
        );

        jdbcTemplate.execute("INSERT INTO messages (\n" +
                "\tthread_id,\n" +
                "\tuser_id,\n" +
                "\tcomment,\n" +
                "\tdt_created\n" +
                ") VALUES (\n" +
                "\t1,\n" +
                "\t2,\n" +
                "\t\"pong\",\n" +
                "\tDATE(NOW())\n" +
                ");"
        );


        jdbcTemplate.execute("CREATE TABLE users (\n" +
                "  id int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  name varchar(255) CHARACTER SET utf8 DEFAULT NULL,\n" +
                "  email varchar(255) DEFAULT NULL,\n" +
                "  passhash varchar(32) DEFAULT NULL,\n" +
                "  dt_created datetime DEFAULT NULL,\n" +
                "  dt_updated datetime DEFAULT NULL,\n" +
                "  is_support tinyint(1) unsigned DEFAULT '0',\n" +
                "  PRIMARY KEY (id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;"
        );

        jdbcTemplate.execute("INSERT INTO users ( \n" +
                "\tname,\n" +
                "\temail,\n" +
                "\tpasshash,\n" +
                "\tdt_created,\n" +
                "\tdt_updated,\n" +
                "\tis_support\n" +
                ") VALUES (\n" +
                "\t\"Admin\",\n" +
                "\t\"admin@example.com\",\n" +
                "\t\"5F4DCC3B5AA765D61D8327DEB882CF99\",\n" +
                "\tDATE(NOW()),\n" +
                "\tDATE(NOW()),\n" +
                "\t1\n" +
                ");"
        );

        jdbcTemplate.execute("INSERT INTO users ( \n" +
                "\tname,\n" +
                "\temail,\n" +
                "\tpasshash,\n" +
                "\tdt_created,\n" +
                "\tdt_updated,\n" +
                "\tis_support\n" +
                ") VALUES (\n" +
                "\t\"Customer\",\n" +
                "\t\"customer@example.com\",\n" +
                "\t\"5F4DCC3B5AA765D61D8327DEB882CF99\",\n" +
                "\tDATE(NOW()),\n" +
                "\tDATE(NOW()),\n" +
                "\t0\n" +
                ");"
        );

    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS messages");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
    }


    @Test
    public void test_getMessage() throws Exception{

        long messageId = 1L;
        Message message;

        //fetches a message from database
        try{

            message = this.messageService.getMessage( messageId );

            assertEquals(true, message.getId().equals( messageId ));
            assertEquals( true, message.getComment().equals( "test" ));
        }
        catch (Exception e){

            fail( e.getMessage() );
        }
    }


    @Test
    public void test_getMessagesByThreadId() throws Exception{

        long threadId = 1L;
        List<MessageView1> messages;

        try{

            messages = this.messageService.getMessagesByThreadId( threadId );

            assertEquals(true, messages.size() > 0 );

            //get first message
            Message m = messages.get(0);

            assertEquals( true, m.getThread_id().equals( threadId ));
        }
        catch (Exception e){

            fail( e.getMessage() );
        }


    }


    @Test
    public void test_addMessageToThread() throws Exception{

        long threadId = 1L;
        long userId = 1L;
        String comment = "testing";
        long messageId;

        //add new message to database
        try{

            messageId = this.messageService.addMessageToThread( threadId, userId, comment );
        }
        catch (Exception e){

            messageId = 0;
            fail( e.getMessage() );
        }

        Message message;

        //fetches a message from database
        try{

            message = this.messageService.getMessage( messageId );

            assertEquals(true, message.getId().equals( messageId ));
            assertEquals( true, message.getThread_id().equals(threadId ));
            assertEquals( true, message.getUser_id().equals(userId ));
            assertEquals( true, message.getComment().equals(comment ));
        }
        catch (Exception e){

            fail( e.getMessage() );
        }
    }


    @Test
    public void check_exceptions(){

        //drop table hence all subsequent calls should result in exception
        jdbcTemplate.execute("DROP TABLE IF EXISTS messages");


        //fetches a message from database
        try{

            this.messageService.getMessage( 1L );
            fail("#1 exception expected here");
        }
        catch (Exception e){

            assertTrue(true);
        }


        //fetches a message from database
        try{

            this.messageService.addMessageToThread( 1L, 1L, "test" );
            fail("#2 exception expected here");
        }
        catch (Exception e){

            assertTrue(true);
        }


        //fetches a message from database
        try{

            this.messageService.getMessagesByThreadId(1L);
            fail("#3 exception expected here");
        }
        catch (Exception e){

            assertTrue(true);
        }
    }
}