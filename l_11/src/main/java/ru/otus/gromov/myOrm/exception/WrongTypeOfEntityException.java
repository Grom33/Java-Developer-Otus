package ru.otus.gromov.myOrm.exception;

public class WrongTypeOfEntityException extends RuntimeException{
    public WrongTypeOfEntityException() {
        super("Wrong data type! Object must be instance of DataSet class!");
    }
}
