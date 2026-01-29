package autoservice.model.exceptions;

public class GarageSpotException extends RuntimeException {
    public GarageSpotException(String message, Exception e) {
        super(message);
    }
}
