package com.tolisso.lab1.exception;

public class ActorException extends RuntimeException {

    public ActorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActorException(Throwable cause) {
        super(cause);
    }
}
