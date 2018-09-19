package ru.otus.gromov;

import java.util.Map;

public interface Atm {

    void deposit(Map<Bill, Integer> moneyBundle);

    Map<Bill, Integer> withdraw(int moneyRequest);

    int getBalance();
}
