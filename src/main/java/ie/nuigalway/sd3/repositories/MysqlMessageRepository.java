package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Repository
@Profile( { "default", "test", "prod" } )
@Transactional
public class MysqlMessageRepository implements MessageRepository {

	//our jdbc tempate
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public MysqlMessageRepository( DataSource dataSource ) {

		jdbcTemplate = new JdbcTemplate( dataSource );
	}


	//mapping sql result rows to Message objects
	private RowMapper<Message> messageMapperLambda = ( rs, rowNum ) -> {

		Message message = new Message();
		message.setId( rs.getLong( "id" ) );
		message.setThread_id( rs.getLong( "thread_id" ) );
		message.setUser_id( rs.getLong( "user_id" ) );
		message.setComment( rs.getString( "comment" ) );
		message.setDt_created( rs.getDate( "dt_created" ) );

		return message;
	};


	//fetch messages by thread_id
	@Override
	public List<Map<String, Object>> getMessagesByThreadId( Long threadId ) {

		String sqlTxt =
			"SELECT messages.*, users.name, users.is_support FROM messages LEFT OUTER JOIN users ON users.id = messages.user_id WHERE thread_id = ? ORDER BY messages.id DESC";
		List<Map<String, Object>> messages;

		//try to fetch all messages
		try {

			messages = jdbcTemplate.queryForList( sqlTxt, threadId );
		}
		catch (InvalidResultSetAccessException e) {
			throw new RuntimeException( e );
		}
		catch (DataAccessException e) {
			throw new RuntimeException( e );
		}


		return messages;
	}


	//create a new message and return autoincremented unique id
	@Override
	public Long addMessageToThread( Long threadId, Long userId, String comment ) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		String    sqlTxt    = "INSERT INTO messages(thread_id,user_id,comment,dt_created) VALUES(?,?,?,?)";


		//current time used at insert time
		java.util.Date dt         = new java.util.Date();
		DateFormat     dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );


		//try to insert entry to mysql
		try {

			jdbcTemplate.update(
				( Connection connection ) -> {
					PreparedStatement ps = connection.prepareStatement( sqlTxt, Statement.RETURN_GENERATED_KEYS );
					ps.setString( 1, Long.toString( threadId ) );
					ps.setString( 2, Long.toString( userId ) );
					ps.setString( 3, comment );
					ps.setString( 4, dateFormat.format( dt ) );

					return ps;
				},
				keyHolder
			                   );
		}
		catch (InvalidResultSetAccessException e) {

			throw new RuntimeException( e );
		}
		catch (DataAccessException e) {

			throw new RuntimeException( e );
		}

		//get the autoincrement id from the insert statement
		Long insertId = keyHolder.getKey().longValue();

		return insertId;
	}
}
