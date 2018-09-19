package ru.otus.gromov.exception;

public class InvalidAmountRequestException extends RuntimeException {
    public InvalidAmountRequestException(String message) {
        super(message);
    }
}
