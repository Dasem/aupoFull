package bookstore.filters;

import org.springframework.messaging.*;
import org.springframework.web.socket.messaging.*;

public class ErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = ex;
        if (exception instanceof MessageDeliveryException) {
            exception = exception.getCause();
        }

        return super.handleClientMessageProcessingError(clientMessage, exception);
    }
}
