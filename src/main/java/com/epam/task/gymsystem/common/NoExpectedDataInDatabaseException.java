package com.epam.task.gymsystem.common;

public class NoExpectedDataInDatabaseException extends Exception {
    public NoExpectedDataInDatabaseException(String message) {
        super(message);
    }
}
