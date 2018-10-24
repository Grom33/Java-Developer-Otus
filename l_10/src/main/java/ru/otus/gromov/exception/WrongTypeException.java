package ru.otus.gromov.exception;

public class WrongTypeException extends RuntimeException{
    public WrongTypeException() {
        super("Wrong data type! Object must be instance of DataSet class!");
    }
}
