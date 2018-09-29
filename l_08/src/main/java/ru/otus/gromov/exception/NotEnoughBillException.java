package ru.otus.gromov.exception;

public class NotEnoughBillException extends RuntimeException{
    public NotEnoughBillException(int moneyRequest) {
        super("There are not enough bills. Requested: "+moneyRequest);
    }
}
