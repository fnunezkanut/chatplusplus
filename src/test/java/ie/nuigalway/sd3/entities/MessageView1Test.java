package ie.nuigalway.sd3.entities;

import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

public class MessageView1Test {


    @Test
    public void check_empty_constructor(){

        MessageView1 messageView1 = new MessageView1();
        assertNull( messageView1.getId() );
    }


    @Test
    public void check_constructor1(){

        long userId = 1L;
        long threadId = 1L;
        String comment = "test";
        boolean is_support = true;
        String name = "somename";
        Date date = new Date();

        MessageView1 messageView1 = new MessageView1( userId, threadId, comment, date, name, is_support );

        assertEquals( true, messageView1.getUser_id().equals( userId ));
        assertEquals( true, messageView1.getThread_id().equals( threadId ));
        assertEquals(true, messageView1.getComment().equals( comment ));
        assertEquals( true, messageView1.getDt_created().equals( date ));
        assertEquals( is_support, messageView1.getIsSupport() );
        assertEquals( true, messageView1.getName().equals( name ));
    }
}