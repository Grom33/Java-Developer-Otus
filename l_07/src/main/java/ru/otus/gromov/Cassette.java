package ru.otus.gromov;

public interface Cassette {
    void deposit(int count);

    void withdraw(int count);

    int getBalance();

    int getBillsCount();
}
