package ru.practicum.shareit.exception;

public class ServerErrorException extends IllegalArgumentException {
    public ServerErrorException(String message) {
        super(message);
    }
}
