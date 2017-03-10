package ie.nuigalway.sd3;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    public ApplicationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
