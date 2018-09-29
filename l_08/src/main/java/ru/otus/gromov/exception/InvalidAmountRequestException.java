package ru.otus.gromov.exception;

public class InvalidAmountRequestException extends RuntimeException {
    public InvalidAmountRequestException(int moneyRequest) {
        super("The requested amount is not a multiple of the minimum value. Requested:" + moneyRequest);
    }
}
