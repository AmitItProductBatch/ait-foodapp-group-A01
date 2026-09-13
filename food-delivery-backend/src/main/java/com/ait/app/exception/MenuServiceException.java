package com.ait.app.exception;

import org.springframework.http.HttpStatusCode;

public class MenuServiceException extends RuntimeException {

    private HttpStatusCode httpStatusCode;
    private String msg;

    public MenuServiceException(HttpStatusCode httpStatusCode, String msg) {
        super(msg);
        this.httpStatusCode = httpStatusCode;
        this.msg = msg;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMsg() {
        return msg;
    }
}