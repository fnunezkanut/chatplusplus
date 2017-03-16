package ie.nuigalway.sd3.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;


public class UserTest {


    //tests all the properties and their getter/setters
    @Test
    public void check_setter_getters() throws Exception {

        User user = new User();

        //user id
        long id = 1L;
        user.setId( id );
        long fetched = user.getId();
        assertEquals( id, fetched );

        //name
        String name = "test";
        user.setName( name );
        assertEquals( true, name.equals( user.getName() ) );

        //email
        String email = "test@example.com";
        user.setEmail( email );
        assertEquals( true, email.equals( user.getEmail() ) );

        //dt created
        Date dt_created = new Date();
        user.setDt_created( dt_created );
        assertEquals( true, dt_created.equals( user.getDt_created() ));

        //pass
        String pass = "012345678901234567890123456789012";
        user.setPass( pass );
        assertEquals( true, pass.equals( user.getPass() ));

        //is_support
        boolean is_support = true;
        user.setIsSupport( is_support );
        assertEquals( is_support, user.getIsSupport() );
    }


    //check two different users with same id are actually same user, ids should be unique
    @Test
    public void check_equality(){

        User user1 = new User();
        user1.setId( 1L );
        User user2 = new User();
        user2.setId( 1L );

        assertEquals(true, user1.equals( user2 ));

        assertEquals( user1.hashCode(), user2.hashCode() );
    }
}