package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Repository
@Profile({"default", "test", "prod"})
@Transactional
public class MysqlUserRepository implements UserRepository {

    //our jdbc template
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MysqlUserRepository(@Qualifier("dataSource") DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //mapping sql result rows to User objects
    private RowMapper<User> userMapperLambda = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPass(rs.getString("passhash"));
        user.setDt_created(rs.getDate("dt_created"));

        short isSupport = rs.getShort("is_support");
        if (isSupport == 1) {
            user.setIsSupport(true);
        } else {
            user.setIsSupport(false);
        }

        return user;
    };

    @Override
    public User getUser(Long id) {

        User user;
        String sqlTxt = "SELECT * FROM users WHERE id=?";

        //try to fetch a single entry from table
        try {

            user = jdbcTemplate.queryForObject(sqlTxt, userMapperLambda, id);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User getUserByEmailAndPasshash(String email, String passhash) {

        User user;
        String sqlTxt = "SELECT * FROM users WHERE email=? AND passhash=?";

        //try to fetch a single entry user from table given email and passhash
        try {

            user = jdbcTemplate.queryForObject(sqlTxt, userMapperLambda, email, passhash);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void updateDtUpdated(Long userId) {

        String sqlTxt = "UPDATE users SET dt_updated=? WHERE id=?";

        //current time used at insert time
        java.util.Date dt = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        //try to update entry to mysql
        try {

            jdbcTemplate.update(sqlTxt, dateFormat.format(dt), userId);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}
