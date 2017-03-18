/**
 * @file ApplicationResponse.java
 *
 * envelope wrapper around json responses, with a status, message and payload fields
 */

package ie.nuigalway.sd3;

import java.util.HashMap;

public class ApplicationResponse{

    private String status = "error";
    private String message = "";
    private HashMap<String, Object> payload = new HashMap<>();


    public ApplicationResponse( String status, String message ) {

        setStatus(status);
        setMessage(message);
    }

    public ApplicationResponse(){}

    public String getStatus() {
        return status;
    }

    //sets status, either ok or error
    public void setStatus(String status) {

        if (status.equals("ok") == true) {

            this.status = status;
        } else {

            this.status = "error";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPayload(HashMap<String, Object> payload) {
        this.payload = payload;
    }

    public HashMap<String, Object> getPayload() {
        return payload;
    }

    //add entry to payload
    public void put(String key, Object val) {
        this.payload.put(key, val);
    }

    //fetches entry from payload (if exists)
    public Object getFromPayload( String key ){
        if( this.payload.containsKey( key) ){
            return this.payload.get( key );
        }
        else{

            return null;
        }
    }
}
