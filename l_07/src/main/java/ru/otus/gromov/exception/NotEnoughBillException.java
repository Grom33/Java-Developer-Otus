package ru.otus.gromov.exception;

public class NotEnoughBillException extends RuntimeException{
    public NotEnoughBillException(String message) {
        super(message);
    }
}
