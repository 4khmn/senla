package autoservice.model.exceptions;

public class IllegalGarageSpotSize extends RuntimeException {
    public IllegalGarageSpotSize(String message) {
        super(message);
    }
    public IllegalGarageSpotSize(String message, Throwable cause) {
        super(message, cause);
    }
}
