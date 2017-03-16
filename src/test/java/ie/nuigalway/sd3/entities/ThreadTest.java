package ie.nuigalway.sd3.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ThreadTest {

    @Test
    public void check_constructor1(){

        long id = 1L;
        String title = "test";
        Thread thread = new Thread(id, title);

        assertEquals( true, thread.getId().equals( id ));
        assertEquals( true, thread.getTitle().equals( title ));
    }


    @Test
    public void check_constructor2(){

        long id = 1L;
        String title = "test";
        Date date = new Date();
        Thread thread = new Thread(id, title, date, date );
        long customerId = 111;
        thread.setCustomerId( customerId );

        assertEquals( true, thread.getDt_created().equals( date ));
        assertEquals( true, thread.getDt_updated().equals( date ));
        assertEquals( true, thread.getCustomerId().equals( customerId ));
    }


    @Test
    public void check_equality(){

        Thread thread1 = new Thread();
        thread1.setId( 1L );
        Thread thread2 = new Thread();
        thread2.setId( 1L );

        assertEquals(true, thread1.equals( thread2 ));

        assertEquals( thread1.hashCode(), thread2.hashCode() );
    }
}