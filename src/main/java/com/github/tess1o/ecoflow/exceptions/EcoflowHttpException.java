package com.github.tess1o.ecoflow.exceptions;

public class EcoflowHttpException extends RuntimeException {

    public EcoflowHttpException(String message) {
        super(message);
    }
    public EcoflowHttpException(Throwable cause) {
        super(cause);
    }
}
