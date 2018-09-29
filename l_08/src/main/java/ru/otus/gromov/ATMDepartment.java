package ru.otus.gromov;

public interface ATMDepartment {

    int getBalance();

    void reloadAllAtm();

    void reloadAtm(int id);

    void addATM(Atm atm);

}
