package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MysqlUserRepositoryTest {

    @Autowired
    private UserService userService;

    //our jdbc template
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {

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
    }

    @After
    public void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
    }


    @Test
    public void check_updateDtUpdated(){

        long userId = 1L;

        try{

            userService.updateDtUpdated( userId );
        }
        catch (Exception e){

            fail( e.getMessage() );
        }


        jdbcTemplate.execute("DROP TABLE IF EXISTS users");

        //check exception
        try{

            userService.updateDtUpdated( userId );
            fail("no users table exists");
        }
        catch (Exception e){

            assert( true );
        }
    }
}