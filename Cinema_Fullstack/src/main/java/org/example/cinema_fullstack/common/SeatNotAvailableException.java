package org.example.cinema_fullstack.common;

public class SeatNotAvailableException extends Exception{
    public SeatNotAvailableException(String message) {
        super(message);
    }
}
