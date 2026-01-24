package autoservice.model.exceptions;

public class OrderException extends RuntimeException {
    public OrderException(String message, Exception ex) {
        super(message);
    }
}
