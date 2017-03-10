package ie.nuigalway.sd3.entities;

import java.util.HashMap;

public class JsonResponse {

	private String                  status = "error";
	private String                  message = "";
	private HashMap<String, String> payload = new HashMap<>();


	public JsonResponse(
		String status,
		String message
	                   ) {

		setStatus( status );
		setMessage( message );
	}

	public String getStatus() {
		return status;
	}

	public void setStatus( String status ) {

		if ( status.equals( "ok" ) == true ) {

			this.status = status;
		}
		else {

			this.status = "error";
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

	public HashMap<String, String> getPayload() {
		return payload;
	}

	public void setPayload( HashMap<String, String> payload ) {
		this.payload = payload;
	}


	public void put(
		String key,
		String val
	               ) {

		this.payload.put( key, val );
	}
}
