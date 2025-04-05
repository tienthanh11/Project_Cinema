package org.example.cinema_fullstack.common;

public class ShowtimeNotAvailableException extends Exception{
    public ShowtimeNotAvailableException(String message) {
        super(message);
    }
}
