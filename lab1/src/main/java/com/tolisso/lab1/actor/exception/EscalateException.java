package com.tolisso.lab1.actor.exception;

public class EscalateException extends RuntimeException {
    public EscalateException() {
    }

    public EscalateException(Throwable cause) {
        super(cause);
    }
}