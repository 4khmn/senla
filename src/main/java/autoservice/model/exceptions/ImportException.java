package autoservice.model.exceptions;

public class ImportException extends RuntimeException {
    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
    public ImportException(String message) {
        super(message);
    }
}
