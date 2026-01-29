package autoservice.model.exceptions;

public class MasterException extends RuntimeException {
    public MasterException(String message, Exception e) {
        super(message);
    }
}
