package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;


@Repository
@Profile({"default", "test", "prod"})
@Transactional
public class MysqlUserRepository implements UserRepository {

    //our jdbc tempate
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MysqlUserRepository(DataSource dataSource) {

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
        } catch (InvalidResultSetAccessException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
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
        } catch (InvalidResultSetAccessException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        //TODO better exceptions

        return user;
    }

    @Override
    public void updateDtUpdated(Long id) {

        //TODO update on login time
    }
}
