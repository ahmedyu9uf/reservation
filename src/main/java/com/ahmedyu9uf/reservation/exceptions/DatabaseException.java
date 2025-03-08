package com.ahmedyu9uf.reservation.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Exception e) {
        super(message, e);
    }
}
