package autoservice.model.exceptions;

import java.io.IOException;

public class ExportException extends RuntimeException {
    public ExportException(String message, IOException e) {
        super(message);
    }
    public ExportException(String message) {
        super(message);
    }
}
