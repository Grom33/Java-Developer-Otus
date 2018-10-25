package ru.otus.gromov.exception;

public class WrongTypeOfEntityException extends RuntimeException{
    public WrongTypeOfEntityException() {
        super("Wrong data type! Object must be instance of DataSet class!");
    }
}
