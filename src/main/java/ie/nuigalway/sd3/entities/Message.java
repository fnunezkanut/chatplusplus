package ie.nuigalway.sd3.entities;

import java.util.Date;

public class Message {

	private Long   id;
	private Long   user_id;
	private Long   thread_id;
	private String comment;
	private Date   dt_created;


	//constructors
	public Message() {
	}

	public Message(
	        Long   user_id,
	        Long   thread_id,
	        String comment,
	        Date   dt_created
	                         ) {
		this.user_id    = user_id;
		this.thread_id  = thread_id;
		this.comment    = comment;
		this.dt_created = dt_created;
	}

	//constructors


	//getters and setters
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id( Long user_id ) {
		this.user_id = user_id;
	}

	public Long getThread_id() {
		return thread_id;
	}

	public void setThread_id( Long thread_id ) {
		this.thread_id = thread_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment( String comment ) {
		this.comment = comment;
	}

	public Date getDt_created() {
		return dt_created;
	}

	public void setDt_created( Date dt_created ) {
		this.dt_created = dt_created;
	}
	//getters and setters


	@Override
	public boolean equals( Object o ) {
		if ( this == o ) return true;
		if ( !( o instanceof Message ) ) return false;

		Message message = (Message) o;

		return getId().equals( message.getId() );
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return "Message{" +
			"id=" + id +
			", user_id=" + user_id +
			", thread_id=" + thread_id +
			", comment='" + comment + '\'' +
			", dt_created=" + dt_created +
			'}';
	}
}
