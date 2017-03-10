package ie.nuigalway.sd3;

import java.io.Serializable;
import java.util.HashMap;

public class ApplicationResponse implements Serializable{

    private String status = "error";
    private String message = "";


    public HashMap<String, Object> getPayload() {
        return payload;
    }

    private HashMap<String, Object> payload = new HashMap<>();


    public ApplicationResponse( String status, String message ) {

        setStatus(status);
        setMessage(message);
    }

    public String getStatus() {
        return status;
    }

    //removes payload
    public ApplicationResponse noPayload() {

        this.payload = null;
        return this;
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


    public void put(String key, Object val) {
        this.payload.put(key, val);
    }
}
