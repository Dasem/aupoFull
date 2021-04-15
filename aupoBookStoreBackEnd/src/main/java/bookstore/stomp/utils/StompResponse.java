package bookstore.stomp.utils;

import org.springframework.http.*;

public class StompResponse <T> {
    T content;
    RequestType requestType;
    int status = HttpStatus.OK.value();
    String customError;

    public StompResponse(RequestType requestType) {
        this.requestType = requestType;
    }

    public StompResponse(T content, RequestType requestType) {
        this.content = content;
        this.requestType = requestType;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomError() {
        return customError;
    }

    public void setCustomError(String customError) {
        this.customError = customError;
    }
}
