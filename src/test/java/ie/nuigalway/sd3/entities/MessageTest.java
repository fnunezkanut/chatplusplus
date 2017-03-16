package ie.nuigalway.sd3.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void check_empty_constructor(){

        Message message = new Message();
        assertNull( message.getId() );
    }


    @Test
    public void check_constructor1(){

        long userId = 1L;
        long threadId = 1L;
        String comment = "test";
        Date date = new Date();

        Message message = new Message( userId, threadId, comment, date );

        assertEquals( true, message.getUser_id().equals( userId ));
        assertEquals( true, message.getThread_id().equals( threadId ));
        assertEquals(true, message.getComment().equals( comment ));
        assertEquals( true, message.getDt_created().equals( date ));

        long id = 100L;
        message.setId( id );
        assertEquals( true, message.getId().equals( id ));
    }



    @Test
    public void check_equality(){

        Message message1 = new Message();
        message1.setId( 1L );
        Message message2 = new Message();
        message2.setId( 1L );

        assertEquals(true, message1.equals( message2 ));

        assertEquals( message1.hashCode(), message2.hashCode() );
    }
}