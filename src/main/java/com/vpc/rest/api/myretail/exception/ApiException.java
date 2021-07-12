package com.vpc.rest.api.myretail.exception;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }

    public ApiException(final String message,final Throwable exe) {
        super(message,exe);
    }

}
