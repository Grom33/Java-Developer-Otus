package ru.otus.gromov.exception;

public class OverloadCassetteException extends RuntimeException {
    public OverloadCassetteException(int Denomination) {
        super("Overloading a cassette with bills. Denomination:" + Denomination);
    }
}
